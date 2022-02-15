package com.epam.jwd.hrmanager.transaction;

import com.epam.jwd.hrmanager.transaction.impl.ThreadLocalTransactionManager;

import java.util.Optional;

public interface TransactionManager {

    void initTransaction();

    void commitTransaction();

    void rollback();

    boolean isTransaction();

    static TransactionManager getInstance() {
        return ThreadLocalTransactionManager.getInstance();
    }

    Optional<TransactionId> getTransactionId();

}
