package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.exception.CouldNotInitialiseConnectionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class ConnectionService implements ConnectionPool {

    private volatile static ConnectionService instance;

    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";

    private final static PropertyContext propertyContext = PropertyContext.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(ConnectionService.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition haveConnections = lock.newCondition();

    private final Queue<ProxyConnection> availableConnections;
    private final List<ProxyConnection> givenAwayConnections;
    private final int MINIMAL_CONNECTIONS_NUM;
    private final long COLLECTOR_TIME_INTERVAL;
    private final double EXPANSION_LEVEL;
    private final double EXPANSION_RATIO;
    private final double COMPRESSION_RATIO;
    private final Consumer<Integer> CONNECTIONS_CREATION = (Integer amount) -> {
        List<ProxyConnection> connections = new ArrayList<>();
        try {
            for (int i = 0; i < amount; i++) {
                Connection connection = DriverManager.getConnection(
                        propertyContext.get(DB_URL),
                        propertyContext.get(DB_USER),
                        propertyContext.get(DB_PASSWORD)
                );
                connections.add(new ProxyConnection(connection, this));
            }
            insertConnections(connections);
        } catch (SQLException e) {
            LOGGER.error("Error occurred creating connection");
        }
    };

    private boolean initialized = false;
    private volatile boolean creatingConnections = false;
    private ConnectionCollector collector;

    private ConnectionService(ConnectionServiceContext serviceContext) {
        MINIMAL_CONNECTIONS_NUM = serviceContext.getMinimalConnectionNum().orElse(8);
        COLLECTOR_TIME_INTERVAL = serviceContext.getCollectorTimeInterval().orElse(60000L);
        EXPANSION_LEVEL = serviceContext.getExpansionLevel().orElse(0.75);
        EXPANSION_RATIO = serviceContext.getExpansionRatio().orElse(0.2);
        COMPRESSION_RATIO = serviceContext.getCompressionRatio().orElse(0.2);
        this.availableConnections = new ArrayDeque<>();
        this.givenAwayConnections = new ArrayList<>();
    }

    public static ConnectionService getInstance(ConnectionServiceContext serviceContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ConnectionService(serviceContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    public static ConnectionService getInstance() {
        return getInstance(ConnectionServiceContext.of().build());
    }


    void init() {
        lock.lock();
        try {
            if (!initialized) {
                registerDrivers();
                initialisationConnections();
                collector = new ConnectionCollector();
                initialized = true;
            }
        } finally {
            lock.unlock();
        }
    }

    void shutDown() {
        lock.lock();
        try {
            if (initialized) {
                deregisterDrivers();
                closeConnections();
                collector.shutDown();
                initialized = false;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isInitialized() {
        lock.lock();
        try {
            return initialized;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Connection takeConnection() throws InterruptedException {
        lock.lock();
        try {
            LOGGER.trace("take connection");
            while (availableConnections.isEmpty()) {
                LOGGER.warn("No free connections!");
                haveConnections.await();
            }
            final ProxyConnection connection = availableConnections.poll();
            givenAwayConnections.add(connection);
            return connection;
        } finally {
            if ((calculateConnectionsAmount() * EXPANSION_LEVEL <= givenAwayConnections.size() && !creatingConnections)) {
                initialisationAdditionalConnections();
            }
            lock.unlock();
        }
    }

    @Override
    public void returnConnection(Connection connection) {
        lock.lock();
        try {
            LOGGER.trace("return connection");
            if (givenAwayConnections.remove((ProxyConnection) connection)) {
                connection.setAutoCommit(true);
                availableConnections.add((ProxyConnection) connection);
                haveConnections.signalAll();
            } else {
                LOGGER.warn("Attempt to add unknown connection to Connection Pool. Connection");
            }
        } catch (SQLException e) {
            LOGGER.error("failed set auto commit mode of connection", e);
        } finally {
            lock.unlock();
        }
    }

    private int calculateConnectionsAmount() {
        return availableConnections.size() + givenAwayConnections.size();
    }

    private void initialisationConnections() {
        try {
            for (int i = 0; i < MINIMAL_CONNECTIONS_NUM; i++) {
                Connection connection = DriverManager.getConnection(
                        propertyContext.get(DB_URL),
                        propertyContext.get(DB_USER),
                        propertyContext.get(DB_PASSWORD)
                );
                availableConnections.add(new ProxyConnection(connection, this));
            }
        } catch (SQLException e) {
            LOGGER.error("Error occurred creating Connection");
            throw new CouldNotInitialiseConnectionService("Failed to create connection", e);
        }
    }

    private void initialisationAdditionalConnections() {
        creatingConnections = true;
        new Thread(() -> CONNECTIONS_CREATION
                .accept((int) (calculateConnectionsAmount() * EXPANSION_RATIO)))
                .start();
    }

    private void insertConnections(List<ProxyConnection> connections) {
        lock.lock();
        try {
            availableConnections.addAll(connections);
        } finally {
            haveConnections.signalAll();
            creatingConnections = false;
            lock.unlock();
        }
    }

    private void closeConnections() {
        closeConnections(this.availableConnections, this.availableConnections.size());
        closeConnections(this.givenAwayConnections, this.givenAwayConnections.size());
    }

    private void closeConnections(Collection<ProxyConnection> connections, int amount) {
        if (connections.size() < amount) {
            throw new IllegalStateException("Incomplete number of removed connections");
        }

        Iterator<ProxyConnection> it = connections.iterator();

        for (int i = 0; i < amount; i++) {
            final ProxyConnection connection = it.next();
            closeConnection(connection);
            connections.remove(connection);
        }
    }

    private void closeConnection(ProxyConnection connection) {
        try {
            connection.realClose();
        } catch (SQLException e) {
            LOGGER.error("Could not close connection", e);
        }
    }

    private void registerDrivers() {
        LOGGER.trace("driver registration");
        try {
            DriverManager.registerDriver(DriverManager.getDriver(propertyContext.get(DB_URL)));
        } catch (SQLException e) {
            throw new CouldNotInitialiseConnectionService("Failed to register driver", e);
        }
    }

    private void deregisterDrivers() {
        LOGGER.trace("deregister drivers");
        final Enumeration<Driver> driverEnumeration = DriverManager.getDrivers();
        while (driverEnumeration.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(driverEnumeration.nextElement());
            } catch (SQLException e) {
                LOGGER.error("could no deregister driver", e);
            }
        }
    }

    private class ConnectionCollector {

        private final Timer timer = new Timer();

        public ConnectionCollector() {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (availableConnections.size() > MINIMAL_CONNECTIONS_NUM) {
                        LOGGER.trace("Compression of connection pool");
                        closeConnections(availableConnections, removedConnectionsNum());
                    }
                }
            };
            timer.schedule(timerTask, COLLECTOR_TIME_INTERVAL, COLLECTOR_TIME_INTERVAL);
        }

        private int removedConnectionsNum() {
            final int removedConnectionsNum = (int) (availableConnections.size() * COMPRESSION_RATIO);
            if (availableConnections.size() - removedConnectionsNum >= MINIMAL_CONNECTIONS_NUM) {
                return removedConnectionsNum;
            } else {
                return availableConnections.size() - MINIMAL_CONNECTIONS_NUM;
            }
        }

        private void shutDown() {
            timer.cancel();
        }
    }
}
