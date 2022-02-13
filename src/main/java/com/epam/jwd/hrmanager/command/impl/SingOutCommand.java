package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;

import java.util.concurrent.locks.ReentrantLock;

public class SingOutCommand implements Command {

    private static final String INDEX_PAGE = "page.index";

    private static final String ACCOUNT_SESSION_ATTRIBUTE = "sessionAccount";
    private static final ReentrantLock lock = new ReentrantLock();
    private static SingOutCommand instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private SingOutCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    static SingOutCommand getInstance(RequestFactory requestFactory, PropertyContext propertyContext){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new SingOutCommand(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if(request.sessionExist() && request.retrieveFromSession(ACCOUNT_SESSION_ATTRIBUTE).isPresent()) {
            request.clearSession();
            return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
        }
        return null;
    }
}
