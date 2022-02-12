package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.exception.InvalidLogPasException;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.secvice.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class SingInCommand implements Command {

    private static final String SING_IN_PAGE = "page.singIn";
    private static final String INDEX_PAGE = "page.index";

    private final static Logger LOGGER = LogManager.getLogger(SingInCommand.class);
    private static final String POSSIBLE_TASK_PARAM_NAME = "task";
    private static final String TASK_PARAM = "taskParam";
    private static final String INVALID_LOGIN_PASSWORD_MESSAGE = "Invalid login or password";
    private static final String AUTHENTICATE_ERROR_ATTRIBUTE = "authenticateError";
    private static final String SESSION_ACCOUNT_SESSION_ATTRIBUTE = "sessionAccount";
    private static final String EMAIL_REQUEST_PARAM_NAME = "email";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final ReentrantLock lock = new ReentrantLock();
    private static SingInCommand instance;

    private final RequestFactory requestFactory;
    private final AccountService accountService;
    private final PropertyContext propertyContext;

    private SingInCommand(RequestFactory requestFactory, AccountService accountService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.accountService = accountService;
        this.propertyContext = propertyContext;
    }

    static SingInCommand getInstance(RequestFactory requestFactory,
                                     AccountService accountService,
                                     PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new SingInCommand(requestFactory, accountService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExist() && request.retrieveFromSession(SESSION_ACCOUNT_SESSION_ATTRIBUTE).isPresent()) {
            return null;
        }
        Optional<Account> account;
        try {
            account = authenticate(request);
        } catch (InvalidLogPasException e) {
            LOGGER.error(INVALID_LOGIN_PASSWORD_MESSAGE);
            request.addAttributeToJsp(AUTHENTICATE_ERROR_ATTRIBUTE, INVALID_LOGIN_PASSWORD_MESSAGE);
            return requestFactory.createForwardResponse(propertyContext.get(SING_IN_PAGE));
        }
        return checkForTasks(request, account.get());
    }

    private Optional<Account> authenticate(CommandRequest request) throws InvalidLogPasException {
        final String email = request.getParameter(EMAIL_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        Optional<Account> account = accountService.authenticate(email, password);
        if (!account.isPresent()) {
            throw new InvalidLogPasException();
        }
        return account;
    }

    private CommandResponse checkForTasks(CommandRequest request, Account account){
        Optional<Object> possibleTask = request.retrieveFromSession(POSSIBLE_TASK_PARAM_NAME);
        Optional<Object> taskParam = request.retrieveFromSession(TASK_PARAM);
        request.createSession();
        request.addToSession(SESSION_ACCOUNT_SESSION_ATTRIBUTE, account);
        if (possibleTask.isPresent() && taskParam.isPresent()) {
            request.addToSession(TASK_PARAM, taskParam.get());
            return requestFactory.createRedirectResponse((String) possibleTask.get());
        }
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }
}
