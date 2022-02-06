package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.secvice.AccountService;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class LoginCommand implements Command {

    private static final String LOGIN_PAGE = "page.login";
    private static final String INDEX_PAGE = "page.index";

    private static final String INVALID_LOGIN_PASSWORD_MESSAGE = "Invalid login or password";
    private static final String ERROR_LOGIN_PASSWORD_ATTRIBUTE = "errorLoginPassMessage";
    private static final String ACCOUNT_SESSION_ATTRIBUTE = "account";
    private static final String LOGIN_REQUEST_PARAM_NAME = "login";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final ReentrantLock lock = new ReentrantLock();
    private static LoginCommand instance;

    private final RequestFactory requestFactory;
    private final AccountService accountService;
    private final PropertyContext propertyContext;

    private LoginCommand(RequestFactory requestFactory, AccountService accountService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.accountService = accountService;
        this.propertyContext = propertyContext;
    }

    static LoginCommand getInstance(RequestFactory requestFactory,
                                    AccountService accountService,
                                    PropertyContext propertyContext){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new LoginCommand(requestFactory, accountService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if(request.sessionExist() && request.retrieveFromSession(ACCOUNT_SESSION_ATTRIBUTE).isPresent()) {
            return null;
        }
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String email = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        Optional<Account> account = accountService.authenticate(login, email, password);
        if(!account.isPresent()){
            request.addAttributeToJsp(ERROR_LOGIN_PASSWORD_ATTRIBUTE, INVALID_LOGIN_PASSWORD_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(LOGIN_PAGE));
        }
        request.createSession();
        request.addToSession(ACCOUNT_SESSION_ATTRIBUTE, account.get());
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }
}
