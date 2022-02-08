package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ShowSingUpPageCommand implements Command {
    public static final String SING_UP_PAGE = "page.singUp";

    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowSingUpPageCommand instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private ShowSingUpPageCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    static ShowSingUpPageCommand getInstance(RequestFactory requestFactory, PropertyContext propertyContext){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new ShowSingUpPageCommand(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(SING_UP_PAGE));
    }
}
