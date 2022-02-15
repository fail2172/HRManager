package com.epam.jwd.hrmanager.command.impl.error;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class UnauthorizedErrorPage implements Command {

    private static final String UNAUTHORIZED_ERROR_PAGE_PROPERTY = "page.unauthorizedError";

    private static final ReentrantLock lock = new ReentrantLock();
    private static UnauthorizedErrorPage instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private UnauthorizedErrorPage(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static UnauthorizedErrorPage getInstance(RequestFactory requestFactory, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new UnauthorizedErrorPage(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(UNAUTHORIZED_ERROR_PAGE_PROPERTY));
    }
}
