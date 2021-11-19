package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.db.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ConnectionPoolFactoryImpl implements ConnectionPoolFactory {

    private final Map<ConnectionPoolType, ConnectionPool> connectionPoolByType = new ConcurrentHashMap<>();

    private ConnectionPoolFactoryImpl(){

    }

    public static ConnectionPoolFactoryImpl getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    public ConnectionPool getBy(ConnectionPoolType type) {
        return connectionPoolByType.computeIfAbsent(type, createConnectionPool());
    }

    private Function<ConnectionPoolType, ConnectionPool> createConnectionPool(){
        return type -> {
            switch (type){
                case SIMPLE_CONNECTION_POOL:
                    return ConnectionService.getInstance();
                case TRANSACTION_CONNECTION_POOL:
                    ConnectionPool connectionPool = getBy(ConnectionPoolType.SIMPLE_CONNECTION_POOL);
                    TransactionManager transactionManager = TransactionManagerFactory.getInstance()
                            .managerFor(TransactionManagerType.SIMPLE_TRANSACTION_MANAGER);
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
