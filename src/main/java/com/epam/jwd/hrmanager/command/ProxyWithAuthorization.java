package com.epam.jwd.hrmanager.command;

import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyWithAuthorization implements InvocationHandler {

    private static final String SESSION_ACCOUNT_PROPERTY = "session.account";
    private static final String COMMAND_UNAUTHORIZED_ERROR_PROPERTY = "command/unauthorized_error";

    private static final Logger LOGGER = LogManager.getLogger(ProxyWithAuthorization.class);
    private static final int FIRST_ARGUMENT = 0;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;
    private final Object command;
    private final Map<Method, Boolean> authorizationAnnotationsByMethods;

    public ProxyWithAuthorization(RequestFactory requestFactory, PropertyContext propertyContext, Object command) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.command = command;
        this.authorizationAnnotationsByMethods = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> T of(T command) {
        final RequestFactory requestFactory = RequestFactory.getInstance();
        final PropertyContext propertyContext = PropertyContext.getInstance();
        return (T) Proxy.newProxyInstance(command.getClass().getClassLoader(),
                command.getClass().getInterfaces(),
                new ProxyWithAuthorization(requestFactory, propertyContext, command));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isAuthorizationAnnotationPresentCaching(method)) {
            return invokeMethodInAuthorization(method, args);
        } else {
            return method.invoke(command, args);
        }
    }

    private Object invokeMethodInAuthorization(Method method, Object[] args) throws InvocationTargetException,
            IllegalAccessException {
        final Object result;
        try {
            final CommandRequest request = (CommandRequest) args[FIRST_ARGUMENT];
            if (!request.retrieveFromSession(propertyContext.get(SESSION_ACCOUNT_PROPERTY)).isPresent()) {
                result = requestFactory.createRedirectResponse(propertyContext.get(COMMAND_UNAUTHORIZED_ERROR_PROPERTY));
            } else {
                result = method.invoke(command, args);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            handleInvocationTargetException(method);
            throw e;
        }
        return result;
    }

    private void handleInvocationTargetException(Method method) {
        LOGGER.error("Method not found. Class: {}, Method: {}",
                command.getClass().getSimpleName(),
                method.getName());
    }

    private boolean isAuthorizationAnnotationPresentCaching(Method method) {
        return authorizationAnnotationsByMethods.computeIfAbsent(method, this::isAuthorizationAnnotationPresent);
    }

    private boolean isAuthorizationAnnotationPresent(Method method) {
        try {
            return command.getClass()
                    .getDeclaredMethod(method.getName(), method.getParameterTypes())
                    .isAnnotationPresent(Authorized.class);
        } catch (NoSuchMethodException ignored) {

        }
        return false;
    }
}
