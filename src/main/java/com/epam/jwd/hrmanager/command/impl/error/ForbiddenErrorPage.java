package com.epam.jwd.hrmanager.command.impl.error;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ForbiddenErrorPage implements Command {

    private static final String FORBIDDEN_ERROR_PAGE_PROPERTY = "page.forbiddenError";

    private static final ReentrantLock lock = new ReentrantLock();
    private static ForbiddenErrorPage instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private ForbiddenErrorPage(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ForbiddenErrorPage getInstance(RequestFactory requestFactory, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ForbiddenErrorPage(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(FORBIDDEN_ERROR_PAGE_PROPERTY));
    }
}
