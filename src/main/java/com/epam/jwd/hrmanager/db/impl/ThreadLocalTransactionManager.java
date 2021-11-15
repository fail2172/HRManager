package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

public class ThreadLocalTransactionManager implements TransactionManager {

    private static final Logger LOGGER = LogManager.getLogger(ThreadLocalTransactionManager.class);

    private static final ThreadLocal<Connection> threadConnection = ThreadLocal.withInitial(() -> {
        try {
            return ConnectionPool.getInstance().takeConnection();
        } catch (InterruptedException e) {
            LOGGER.warn("Thread was interrupted");
            Thread.currentThread().interrupt();
            return null;
        }
    });

    @Override
    public void initTransaction() {

    }

    @Override
    public void commitTransaction() {

    }

    @Override
    public boolean isTransaction() {
        return false;
    }
}
