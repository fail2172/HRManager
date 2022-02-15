package com.epam.jwd.hrmanager.command.impl.action;

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

    private static final String SESSION_ACCOUNT_PROPERTY = "session.account";
    private static final String SESSION_ERROR_MESSAGE_PROPERTY = "session.error_message";
    private static final String SESSION_TASK_PROPERTY = "session.task";
    private static final String SESSION_TASK_PARAM_PROPERTY = "session.task_param";

    private static final String COMMAND_FORBIDDEN_ERROR_PROPERTY = "command/forbidden_error";
    private static final String COMMAND_MAIN_PAGE_PROPERTY = "command/main_page";
    private static final String COMMAND_SING_IN_PAGE_PROPERTY = "command/sing_in_page";

    private static final String EMAIL_REQUEST_PARAM = "email";
    private static final String PASSWORD_REQUEST_PARAM = "password";

    private final static Logger LOGGER = LogManager.getLogger(SingInCommand.class);
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

    public static SingInCommand getInstance(RequestFactory requestFactory,
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
        if (request.sessionExist() && request.retrieveFromSession(propertyContext.get(SESSION_ACCOUNT_PROPERTY)).isPresent()) {
            return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_FORBIDDEN_ERROR_PROPERTY));
        }
        final Account account;
        try {
            account = authenticate(request);
        } catch (InvalidLogPasException e) {
            LOGGER.error(e);
            request.addToSession(propertyContext.get(SESSION_ERROR_MESSAGE_PROPERTY), e.getMessage());
            return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_SING_IN_PAGE_PROPERTY));
        }
        return checkForTasks(request, account);
    }

    private Account authenticate(CommandRequest request) throws InvalidLogPasException {
        final String email = request.getParameter(EMAIL_REQUEST_PARAM);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM);
        Optional<Account> account = accountService.authenticate(email, password);
        if (!account.isPresent()) {
            throw new InvalidLogPasException("Invalid login or password");
        } else {
            return account.get();
        }
    }

    private CommandResponse checkForTasks(CommandRequest request, Account account) {
        final Optional<Object> possibleTask = request.retrieveFromSession(propertyContext.get(SESSION_TASK_PROPERTY));
        final Optional<Object> taskParam = request.retrieveFromSession(propertyContext.get(SESSION_TASK_PARAM_PROPERTY));
        request.createSession();
        request.addToSession(propertyContext.get(SESSION_ACCOUNT_PROPERTY), account);
        if (possibleTask.isPresent() && taskParam.isPresent()) {
            request.addToSession(propertyContext.get(SESSION_TASK_PROPERTY), taskParam.get());
            return requestFactory.createRedirectResponse((String) possibleTask.get());
        }
        return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_MAIN_PAGE_PROPERTY));
    }
}
