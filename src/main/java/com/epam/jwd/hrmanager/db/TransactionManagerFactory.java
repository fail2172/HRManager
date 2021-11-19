package com.epam.jwd.hrmanager.db;

import com.epam.jwd.hrmanager.db.impl.TransactionManagerFactoryImpl;

public interface TransactionManagerFactory {

    TransactionManager managerFor(TransactionManagerType type);

    static TransactionManagerFactory getInstance(){
        return TransactionManagerFactoryImpl.getInstance();
    }

}
