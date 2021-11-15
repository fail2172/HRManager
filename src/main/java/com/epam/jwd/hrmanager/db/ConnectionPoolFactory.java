package com.epam.jwd.hrmanager.db;

import com.epam.jwd.hrmanager.db.impl.ConnectionPoolFactoryImpl;

public interface ConnectionPoolFactory {

    ConnectionPool getBy(ConnectionPoolType type);

    static ConnectionPoolFactory getInstance(){
        return ConnectionPoolFactoryImpl.getInstance();
    }

}
