package com.epam.jwd.hrmanager.db;

import java.sql.Connection;

public interface ConnectionPool {

    boolean isInitialized();

    Connection takeConnection() throws InterruptedException;

    void returnConnection(Connection connection);

}
