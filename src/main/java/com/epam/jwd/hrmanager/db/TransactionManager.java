package com.epam.jwd.hrmanager.db;

public interface TransactionManager {

    void initTransaction();

    void commitTransaction();

    boolean isTransaction();

    static TransactionManager getInstance(){
        return null;
    }

}
