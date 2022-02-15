package com.epam.jwd.hrmanager.command.impl.error;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ServerErrorPage implements Command {

    private static final String SERVER_ERROR_PAGE_PROPERTY = "page.serverError";

    private static final ReentrantLock lock = new ReentrantLock();
    private static ServerErrorPage instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private ServerErrorPage(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ServerErrorPage getInstance(RequestFactory requestFactory, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ServerErrorPage(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(SERVER_ERROR_PAGE_PROPERTY));
    }
}
