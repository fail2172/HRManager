package com.epam.jwd.hrmanager.transaction;

import com.epam.jwd.hrmanager.transaction.impl.TransactionManagerFactoryImpl;

public interface TransactionManagerFactory {

    TransactionManager managerFor(TransactionManagerType type);

    static TransactionManagerFactory getInstance() {
        return TransactionManagerFactoryImpl.getInstance();
    }

}
