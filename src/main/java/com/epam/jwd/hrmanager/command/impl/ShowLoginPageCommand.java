package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ShowLoginPageCommand implements Command {

    private static final String LOGIN_JSP_PATH = "/WEB-INF/jsp/login.jsp";
    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowLoginPageCommand instance;

    private final RequestFactory requestFactory;

    private ShowLoginPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    static ShowLoginPageCommand getInstance(RequestFactory requestFactory){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new ShowLoginPageCommand(requestFactory);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(LOGIN_JSP_PATH);
    }
}
