package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;

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
                    return ConnectionPool.getInstance();
                case TRANSACTION_CONNECTION_POOL:
                    return ConnectionPool.transactional();
                default:
                    throw new IllegalArgumentException("No such type of connection pool");
            }
        };
    }

    private static class Holder {
        private static final ConnectionPoolFactoryImpl INSTANCE = new ConnectionPoolFactoryImpl();
    }
}
