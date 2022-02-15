package com.epam.jwd.hrmanager.command.impl.error;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class NotFoundErrorPage implements Command {

    private static final String NOT_FOUND_ERROR_PAGE_PROPERTY = "page.notFoundError";

    private static final ReentrantLock lock = new ReentrantLock();
    private static NotFoundErrorPage instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private NotFoundErrorPage(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static NotFoundErrorPage getInstance(RequestFactory requestFactory, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new NotFoundErrorPage(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(NOT_FOUND_ERROR_PAGE_PROPERTY));
    }
}
