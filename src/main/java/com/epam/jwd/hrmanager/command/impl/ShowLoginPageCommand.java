package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;

import java.util.concurrent.locks.ReentrantLock;

public class ShowLoginPageCommand implements Command {

    public static final String LOGIN_PAGE = "page.login";

    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowLoginPageCommand instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private ShowLoginPageCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    static ShowLoginPageCommand getInstance(RequestFactory requestFactory, PropertyContext propertyContext){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new ShowLoginPageCommand(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(LOGIN_PAGE));
    }
}
