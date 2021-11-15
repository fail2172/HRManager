package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.DaoFactory;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.dao.UserDao;
import com.epam.jwd.hrmanager.model.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DaoFactoryImpl implements DaoFactory {

    private static final String DAO_NOT_FOUND = "Could not create dao for %s class";

    private final Map<Class<?>, EntityDao<?>> daoByEntity = new ConcurrentHashMap<>();

    private DaoFactoryImpl() {

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
                    return UserDao.getInstance();
                case "City":
                    return MethodCityDao.getInstance();
                case "Street":
                    return MethodStreetDao.getInstance();
                case "Address":
                    return MethodAddressDao.getInstance();
                case "Employer":
                    return MethodEmployerDao.getInstance();
                case "Vacancy":
                    return MethodVacancyDao.getInstance();
                case "Interview":
                    return MethodInterviewDao.getInstance();
                default:
                    throw new IllegalStateException(String.format(DAO_NOT_FOUND, className));
            }
        };
    }

    private static class Holder {
        private static final DaoFactoryImpl INSTANCE = new DaoFactoryImpl();
    }
}
