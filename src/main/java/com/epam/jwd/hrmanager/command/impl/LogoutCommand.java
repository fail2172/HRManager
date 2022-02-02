package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.secvice.AccountService;

import java.util.concurrent.locks.ReentrantLock;

public class LogoutCommand implements Command {

    private static final String MAIN_JSP_PATH = "/WEB-INF/jsp/main.jsp";
    private static final String ACCOUNT_SESSION_ATTRIBUTE = "account";
    private static final ReentrantLock lock = new ReentrantLock();
    private static LogoutCommand instance;

    private final RequestFactory requestFactory;

    private LogoutCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    static LogoutCommand getInstance(RequestFactory requestFactory){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new LogoutCommand(requestFactory);
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
            return requestFactory.createRedirectResponse(MAIN_JSP_PATH);
        }
        return null;
    }
}
