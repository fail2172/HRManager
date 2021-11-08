package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.dao.UserDao;
import com.epam.jwd.hrmanager.model.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ServiceFactoryImpl implements ServiceFactory {

    private static final String SERVICE_NOT_FOUND = "Could not create service for %s class";
    private final Map<Class<?>, EntityService<?>> serviceByEntity = new ConcurrentHashMap<>();

    private ServiceFactoryImpl() {

    }

    static ServiceFactoryImpl getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> EntityService<T> serviceFor(Class<T> modelClass) {
        return (EntityService<T>) serviceByEntity
                .computeIfAbsent(modelClass, createServiceFromClass());
    }

    private Function<Class<?>, EntityService<?>> createServiceFromClass() {
        return clazz -> {
            final String className = clazz.getSimpleName();
            switch (className) {
                case "User":
                    return new UserService(UserDao.getInstance());
                default:
                    throw new IllegalStateException(String.format(SERVICE_NOT_FOUND, className));
            }
        };
    }

    private static class Holder {
        public static ServiceFactoryImpl INSTANCE = new ServiceFactoryImpl();
    }
}
