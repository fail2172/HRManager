package com.epam.jwd.hrmanager.db;

import com.epam.jwd.hrmanager.db.impl.ConnectionService;
import com.epam.jwd.hrmanager.db.impl.ConnectionServiceContext;
import com.epam.jwd.hrmanager.db.impl.TransactionConnectionPool;
import com.epam.jwd.hrmanager.exeption.CouldNotInitialiseConnectionService;

import java.sql.Connection;

public interface ConnectionPool {

    boolean init() throws CouldNotInitialiseConnectionService;

    boolean shutDown();

    boolean isInitialized();

    Connection takeConnection() throws InterruptedException;

    void returnConnection(Connection connection);

    static ConnectionService getInstance(ConnectionServiceContext context){
        return ConnectionService.getInstance(context);
    }

    static ConnectionPool getInstance(){
        return ConnectionService.getInstance();
    }

    static ConnectionPool transactional() {
        return TransactionConnectionPool.getInstance();
    }

}
