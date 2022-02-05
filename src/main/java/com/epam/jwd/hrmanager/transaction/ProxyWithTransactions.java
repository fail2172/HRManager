package com.epam.jwd.hrmanager.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyWithTransactions implements InvocationHandler {

    private static final Logger LOGGER = LogManager.getLogger(ProxyWithTransactions.class);

    private final TransactionManager transactionManager;
    private final Object service;
    private final Map<Method, Boolean> transactionalAnnotationsByMethods;

    public ProxyWithTransactions(TransactionManager transactionManager, Object service) {
        this.transactionManager = transactionManager;
        this.service = service;
        this.transactionalAnnotationsByMethods = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> T of(T service) {
        final TransactionManagerFactory factory = TransactionManagerFactory.getInstance();
        final TransactionManager transactionManager = factory.managerFor(TransactionManagerType.SIMPLE_MANAGER);
        return (T) Proxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new ProxyWithTransactions(transactionManager, service));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isTransactionalAnnotationPresentCaching(method)) {
            return invokeMethodInTransaction(method, args);
        } else {
            return method.invoke(service, args);
        }
    }

    private Object invokeMethodInTransaction(Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException {
        final Object result;
        try {
            LOGGER.trace("transaction started");
            transactionManager.initTransaction();
            result = method.invoke(service, args);
            transactionManager.commitTransaction();
            LOGGER.trace("end of transaction");
        } catch (InvocationTargetException | IllegalAccessException e) {
            handleInvocationTargetException(method);
            throw e;
        }
        return result;
    }

    private void handleInvocationTargetException(Method method) {
        LOGGER.error("Method not found. Class: {}, Method: {}",
                service.getClass().getSimpleName(),
                method.getName());
        transactionManager.rollback();
        transactionManager.commitTransaction();
        LOGGER.info("transaction committed because error occurred");
    }

    private boolean isTransactionalAnnotationPresentCaching(Method method) {
        return transactionalAnnotationsByMethods.computeIfAbsent(method, this::isTransactionalAnnotationPresent);
    }

    private boolean isTransactionalAnnotationPresent(Method method) {
        try {
            return service.getClass()
                    .getDeclaredMethod(method.getName(), method.getParameterTypes())
                    .isAnnotationPresent(Transactional.class);
        } catch (NoSuchMethodException e) {
            LOGGER.error("Method not found. Class: {}, Method: {}",
                    service.getClass().getSimpleName(),
                    method.getName());
        }
        return false;
    }
}