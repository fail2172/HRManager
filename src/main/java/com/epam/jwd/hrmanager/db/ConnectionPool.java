package com.epam.jwd.hrmanager.db;

import com.epam.jwd.hrmanager.exeption.CouldNotInitialisationConnectionService;

import java.sql.Connection;

public interface ConnectionPool {

    boolean init() throws CouldNotInitialisationConnectionService;

    boolean shutDown();

    boolean isInitialized();

    Connection takeConnection() throws InterruptedException;

    void returnConnection(Connection connection);

}
