package com.epam.jwd.hrmanager.command.impl.action;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.exception.FillingOutTheFormException;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.AccountStatus;
import com.epam.jwd.hrmanager.model.Role;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.AccountService;
import com.epam.jwd.hrmanager.secvice.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class SingUpCommand implements Command {

    private static final String SESSION_ACCOUNT_PROPERTY = "session.account";
    private static final String SESSION_ERROR_MESSAGE_PROPERTY = "session.error_message";
    private static final String SESSION_TASK_PROPERTY = "session.task";
    private static final String SESSION_TASK_PARAM_PROPERTY = "session.task_param";

    private static final String COMMAND_MAIN_PAGE_PROPERTY = "command/main_page";
    private static final String COMMAND_SING_UP_PAGE_PROPERTY = "command/sing_up_page";

    private static final String PASSWORD_LENGTH_ERROR = "password must be received at least 8 characters";
    private static final String PASSWORD_MISMATCH_ERROR = "password mismatch";
    private static final String LOGIN_BUSY_ERROR = "login busy";
    private static final String EMAIL_BUSY_ERROR = "email busy";

    private static final String LOGIN_REQUEST_PARAM = "login";
    private static final String EMAIL_REQUEST_PARAM = "email";
    private static final String FIRST_NAME_REQUEST_PARAM = "firstName";
    private static final String SECOND_NAME_REQUEST_PARAM = "secondName";
    private static final String PASSWORD_REQUEST_PARAM = "password";
    private static final String REPEAT_PASSWORD_REQUEST_PARAM = "repeatPassword";

    private static final Logger LOGGER = LogManager.getLogger(SingUpCommand.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static SingUpCommand instance;

    private final RequestFactory requestFactory;
    private final AccountService accountService;
    private final UserService userService;
    private final PropertyContext propertyContext;

    private SingUpCommand(RequestFactory requestFactory, AccountService accountService,
                          UserService userService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.accountService = accountService;
        this.userService = userService;
        this.propertyContext = propertyContext;
    }

    public static SingUpCommand getInstance(RequestFactory requestFactory,
                                            AccountService accountService,
                                            UserService userService,
                                            PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new SingUpCommand(requestFactory, accountService, userService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Account account;
        try {
            account = createAccount(request);
        } catch (FillingOutTheFormException e) {
            LOGGER.error(e);
            request.addToSession(propertyContext.get(SESSION_ERROR_MESSAGE_PROPERTY), e.getMessage());
            return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_SING_UP_PAGE_PROPERTY));
        }
        return checkForTasks(request, account);
    }

    private Account createAccount(CommandRequest request) throws FillingOutTheFormException {
        final String login = request.getParameter(LOGIN_REQUEST_PARAM);
        final String email = request.getParameter(EMAIL_REQUEST_PARAM);
        final String firstName = request.getParameter(FIRST_NAME_REQUEST_PARAM);
        final String secondName = request.getParameter(SECOND_NAME_REQUEST_PARAM);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM);
        final String repeatPassword = request.getParameter(REPEAT_PASSWORD_REQUEST_PARAM);

        validate(login, email, password, repeatPassword);

        final User user = userService.add(new User(firstName, secondName));
        return accountService.add(new Account(login, email, password, user, Role.ASPIRANT, AccountStatus.UNBANNED));
    }

    private void validate(String login, String email, String password, String repeatPassword)
            throws FillingOutTheFormException {
        if (accountService.findByEmail(email).isPresent()) {
            throw new FillingOutTheFormException(EMAIL_BUSY_ERROR);
        } else if (accountService.findByLogin(login).isPresent()) {
            throw new FillingOutTheFormException(LOGIN_BUSY_ERROR);
        } else if (!password.equals(repeatPassword)) {
            throw new FillingOutTheFormException(PASSWORD_MISMATCH_ERROR);
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new FillingOutTheFormException(PASSWORD_LENGTH_ERROR);
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
