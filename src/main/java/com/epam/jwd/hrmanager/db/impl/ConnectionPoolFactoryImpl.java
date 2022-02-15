package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.db.*;
import com.epam.jwd.hrmanager.transaction.TransactionManager;
import com.epam.jwd.hrmanager.transaction.TransactionManagerFactory;
import com.epam.jwd.hrmanager.transaction.TransactionManagerType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ConnectionPoolFactoryImpl implements ConnectionPoolFactory {

    private final Map<ConnectionPoolType, ConnectionPool> connectionPoolByType;

    private ConnectionPoolFactoryImpl() {
        connectionPoolByType = new ConcurrentHashMap<>();
    }

    public static ConnectionPoolFactoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public ConnectionPool getBy(ConnectionPoolType type) {
        return connectionPoolByType.computeIfAbsent(type, createConnectionPool());
    }

    private Function<ConnectionPoolType, ConnectionPool> createConnectionPool() {
        return type -> {
            switch (type) {
                case SIMPLE_CONNECTION_POOL:
                    return ConnectionService.getInstance();
                case TRANSACTION_CONNECTION_POOL:
                    final ConnectionPool connectionPool = getBy(ConnectionPoolType.SIMPLE_CONNECTION_POOL);
                    final TransactionManager transactionManager = TransactionManagerFactory.getInstance()
                            .managerFor(TransactionManagerType.SIMPLE_MANAGER);
                    return TransactionConnectionService.getInstance(connectionPool, transactionManager);
                default:
                    throw new IllegalArgumentException("No such type of connection pool");
            }
        };
    }

    private static class Holder {
        private static final ConnectionPoolFactoryImpl INSTANCE = new ConnectionPoolFactoryImpl();
    }
}
