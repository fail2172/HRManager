package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.DaoFactory;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;
import com.epam.jwd.hrmanager.model.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DaoFactoryImpl implements DaoFactory {

    private static final String DAO_NOT_FOUND = "Could not create dao for %s class";

    private final Map<Class<?>, EntityDao<?>> daoByEntity = new ConcurrentHashMap<>();
    private final ConnectionPool connectionPool;

    private DaoFactoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static DaoFactoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> EntityDao<T> daoFor(Class<T> modelDao) {
        return (EntityDao<T>) daoByEntity.computeIfAbsent(modelDao, createDaoForEntity());
    }

    private Function<Class<?>, EntityDao<?>> createDaoForEntity() {
        return clazz -> {
            final String className = clazz.getSimpleName();
            switch (className) {
                case "User":
                    return MethodUserDao.getInstance(connectionPool);
                case "City":
                    return MethodCityDao.getInstance(connectionPool);
                case "Street":
                    return MethodStreetDao.getInstance(connectionPool);
                case "Address":
                    return MethodAddressDao.getInstance(connectionPool);
                case "Employer":
                    return MethodEmployerDao.getInstance(connectionPool);
                case "Vacancy":
                    return MethodVacancyDao.getInstance(connectionPool);
                case "Interview":
                    return MethodInterviewDao.getInstance(connectionPool);
                case "Account":
                    return MethodAccountDao.getInstance(connectionPool);
                case "JobRequest":
                    return MethodJobRequestDao.getInstance(connectionPool);
                default:
                    throw new IllegalStateException(String.format(DAO_NOT_FOUND, className));
            }
        };
    }

    private static class Holder {
        private static final DaoFactoryImpl INSTANCE = new DaoFactoryImpl(ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.TRANSACTION_CONNECTION_POOL));
    }
}
