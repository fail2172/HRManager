package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.exeption.CouldNotInitialiseConnectionService;

import java.sql.Connection;

public final class TransactionConnectionPool implements ConnectionPool {

    private final ConnectionPool connectionPool;

    private TransactionConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static TransactionConnectionPool getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    public boolean init() throws CouldNotInitialiseConnectionService {
        return connectionPool.init();
    }

    @Override
    public boolean shutDown() {
        return connectionPool.shutDown();
    }

    @Override
    public boolean isInitialized() {
        return connectionPool.isInitialized();
    }

    @Override
    public Connection takeConnection() throws InterruptedException {
        return connectionPool.takeConnection();
    }

    @Override
    public void returnConnection(Connection connection) {
        connectionPool.returnConnection(connection);
    }

    public static ConnectionService getInstance(ConnectionServiceContext context) {
        return ConnectionPool.getInstance(context);
    }

    private static class Holder {
        private static final TransactionConnectionPool INSTANCE = new TransactionConnectionPool(ConnectionPool.getInstance());
    }
}
