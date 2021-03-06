package com.epam.jwd.hrmanager.transaction.impl;

import com.epam.jwd.hrmanager.transaction.TransactionManager;
import com.epam.jwd.hrmanager.transaction.TransactionManagerFactory;
import com.epam.jwd.hrmanager.transaction.TransactionManagerType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class TransactionManagerFactoryImpl implements TransactionManagerFactory {

    private static final String DAO_NOT_FOUND = "Could not create transaction manager for %s type";

    private final Map<TransactionManagerType, TransactionManager> managerByType = new ConcurrentHashMap<>();

    public static TransactionManagerFactoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public TransactionManager managerFor(TransactionManagerType type) {
        return managerByType.computeIfAbsent(type, createTransactionManager());
    }

    private Function<TransactionManagerType, TransactionManager> createTransactionManager() {
        return type -> {
            if (type == TransactionManagerType.SIMPLE_MANAGER) {
                return ThreadLocalTransactionManager.getInstance();
            }
            throw new IllegalStateException(String.format(DAO_NOT_FOUND, type));
        };
    }

    private static class Holder {
        private static final TransactionManagerFactoryImpl INSTANCE = new TransactionManagerFactoryImpl();
    }
}
