package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.TransactionId;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.exeption.CouldNotInitialiseConnectionService;

import java.sql.Connection;
import java.util.Optional;

public final class TransactionConnectionPool implements ConnectionPool {

    private final ConnectionPool connectionPool;
    private final TransactionManager transactionManager;

    private TransactionConnectionPool(ConnectionPool connectionPool, TransactionManager transactionManager) {
        this.connectionPool = connectionPool;
        this.transactionManager = transactionManager;
    }

    public static TransactionConnectionPool getInstance() {
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
        Optional<TransactionId> transactionId = transactionManager.getTransactionId();
        return transactionId.isPresent()
                ? transactionId.get().getConnection()
                : new ProxyConnection(connectionPool.takeConnection(), this);
    }

    @Override
    public void returnConnection(Connection connection) {
        Optional<TransactionId> transactionId = transactionManager.getTransactionId();
        if (!transactionId.isPresent()){
            connectionPool.returnConnection(((ProxyConnection) connection).getConnection());
        }
    }

    public static ConnectionService getInstance(ConnectionServiceContext context) {
        return ConnectionPool.getInstance(context);
    }

    private static class Holder {
        private static final TransactionConnectionPool INSTANCE =
                new TransactionConnectionPool(ConnectionPool.getInstance(), TransactionManager.getInstance());
    }
}
