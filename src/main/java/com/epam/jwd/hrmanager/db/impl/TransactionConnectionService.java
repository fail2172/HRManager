package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.TransactionId;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.exeption.CouldNotInitialiseConnectionService;

import java.sql.Connection;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public final class TransactionConnectionService implements ConnectionPool {

    private static final ReentrantLock lock = new ReentrantLock();

    private static TransactionConnectionService instance;
    private final ConnectionPool connectionPool;
    private final TransactionManager transactionManager;

    private TransactionConnectionService(ConnectionPool connectionPool, TransactionManager transactionManager) {
        this.connectionPool = connectionPool;
        this.transactionManager = transactionManager;
    }

    public static TransactionConnectionService getInstance(ConnectionPool connectionPool,
                                                           TransactionManager transactionManager) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new TransactionConnectionService(connectionPool, transactionManager);
                }
            }
            lock.unlock();
        }
        return instance;
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
}
