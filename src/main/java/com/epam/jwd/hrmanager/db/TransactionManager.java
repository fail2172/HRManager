package com.epam.jwd.hrmanager.db;

import com.epam.jwd.hrmanager.db.impl.ThreadLocalTransactionManager;

import java.util.Optional;

public interface TransactionManager {

    void initTransaction();

    void commitTransaction();

    boolean isTransaction();

    static TransactionManager getInstance(){
        return ThreadLocalTransactionManager.getInstance();
    }

    Optional<TransactionId> getTransactionId();

}
