package com.epam.jwd.hrmanager.db;

import com.epam.jwd.hrmanager.exeption.CouldNotInitialisationConnectionService;
import com.epam.jwd.hrmanager.exeption.NotIllegalParameters;
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
import java.util.function.Function;

public class ConnectionService implements ConnectionPool {

    private static final Logger LOG = LogManager.getLogger(ConnectionService.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition haveConnections = lock.newCondition();

    private volatile static ConnectionService instance;

    private final Queue<ProxyConnection> availableConnections;
    private final List<ProxyConnection> givenAwayConnections;
    private final String DB_URL = "jdbc:mysql://localhost:3306/jwd";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "12345678";
    private final Consumer<Integer> CONNECTIONS_CREATION = (Integer amount) -> {
        List<ProxyConnection> connections = new ArrayList<>();
        try {
            for (int i = 0; i < amount; i++) {
                LOG.trace("Open connection");
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                connections.add(new ProxyConnection(connection, this));
            }
            insertConnections(connections);
        } catch (SQLException e) {
            LOG.error("Error occurred creating Connection");
        }
    };


    private boolean initialized = false;
    private volatile boolean creatingConnections = false;
    private int MINIMAL_CONNECTIONS_NUM = 8;

    private ConnectionService() {
        availableConnections = new ArrayDeque<>();
        givenAwayConnections = new ArrayList<>();
    }

    public static ConnectionService getInstance() {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ConnectionService();
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public boolean init() {
        lock.lock();
        try {
            if (!initialized) {
                registerDrivers();
                initialisationConnections(MINIMAL_CONNECTIONS_NUM, true);
                initialized = true;
                lock.unlock();
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean shutDown() {
        lock.lock();
        try {
            if (initialized) {
                deregisterDrivers();
                closeConnections();
                initialized = false;
                return true;
            }
            return false;
        } catch (NotIllegalParameters notIllegalParameters) {
            LOG.error("Incorrect connection closing");
            notIllegalParameters.printStackTrace();
            return true;
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
            while (availableConnections.isEmpty()) {
                haveConnections.await();
            }
            final ProxyConnection connection = availableConnections.poll();
            givenAwayConnections.add(connection);
            return connection;
        } finally {
            if ((calculateConnectionsAmount() * 0.75 == givenAwayConnections.size() && !creatingConnections)) {
                initialisationConnections();
            }
            lock.unlock();
        }
    }

    @Override
    public void returnConnection(Connection connection) {
        lock.lock();
        try {
            if (givenAwayConnections.remove((ProxyConnection) connection)) {
                availableConnections.add((ProxyConnection) connection);
                haveConnections.signalAll();
            } else {
                LOG.warn("Attempt to add unknown connection to Connection Pool. Connection");
            }
        } finally {
            lock.unlock();
        }
    }

    private int calculateConnectionsAmount(){
        return availableConnections.size() + givenAwayConnections.size();
    }

    private void initialisationConnections(int amount, boolean firstInitialisationConnections) {
        try {
            for (int i = 0; i < amount; i++) {
                LOG.trace("Open connection");
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                availableConnections.add(new ProxyConnection(connection, this));
            }
        } catch (SQLException e) {
            LOG.error("Error occurred creating Connection");
            if (firstInitialisationConnections) {
                throw new CouldNotInitialisationConnectionService("Failed to create connection", e);
            }
        }
    }

    private void initialisationConnections() {
        creatingConnections = true;
        new Thread(() -> CONNECTIONS_CREATION
                .accept((int) (calculateConnectionsAmount() * 1.5)))
                .start();
    }

    private void insertConnections(List<ProxyConnection> connections) {
        lock.lock();
        try {
            availableConnections.addAll(connections);
        } finally {
            lock.unlock();
            haveConnections.signalAll();
            creatingConnections = false;
        }
    }

    private void closeConnections() throws NotIllegalParameters {
        closeConnections(this.availableConnections, this.availableConnections.size());
        closeConnections(this.givenAwayConnections, this.givenAwayConnections.size());
    }

    private void closeConnections(Collection<ProxyConnection> connections, int amount) throws NotIllegalParameters {
        if (connections.size() < amount) {
            throw new NotIllegalParameters("Incomplete number of removed connections");
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
            LOG.trace("Close connection");
            connection.realClose();
        } catch (SQLException e) {
            LOG.error("Could not close connection", e);
        }
    }

    private void registerDrivers() {
        LOG.trace("driver registration");
        try {
            DriverManager.registerDriver(DriverManager.getDriver(DB_URL));
        } catch (SQLException e) {
            throw new CouldNotInitialisationConnectionService("Failed to register driver", e);
        }
    }

    private void deregisterDrivers() {
        LOG.trace("deregister drivers");
        final Enumeration<Driver> driverEnumeration = DriverManager.getDrivers();
        while (driverEnumeration.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(driverEnumeration.nextElement());
            } catch (SQLException e) {
                LOG.error("could no deregister driver", e);
            }
        }
    }
}
