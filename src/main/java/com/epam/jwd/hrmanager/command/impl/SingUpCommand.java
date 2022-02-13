package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.AccountStatus;
import com.epam.jwd.hrmanager.model.Role;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.AccountService;
import com.epam.jwd.hrmanager.secvice.UserService;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class SingUpCommand implements Command {

    private static final String SING_UP_PAGE = "page.singUp";
    private static final String INDEX_PAGE = "page.index";

    private static final String PASSWORD_LENGTH_ERROR = "password length";
    private static final String PASSWORD_MISMATCH_ERROR = "password mismatch";
    private static final String LOGIN_BUSY_ERROR = "login busy";
    private static final String EMAIL_BUSY_ERROR = "email busy";
    private static final String ERROR_REGISTRATION_ATTRIBUTE = "errorRegistrationMessage";
    private static final String ACCOUNT_SESSION_ATTRIBUTE = "account";
    private static final String LOGIN_REQUEST_PARAM_NAME = "login";
    private static final String EMAIL_REQUEST_PARAM_NAME = "email";
    private static final String FIRST_NAME_REQUEST_PARAM_NAME = "firstName";
    private static final String SECOND_NAME_REQUEST_PARAM_NAME = "secondName";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String REPEAT_PASSWORD_REQUEST_PARAM_NAME = "repeatPassword";
    private static final String POSSIBLE_TASK_PARAM_NAME = "task";
    private static final String TASK_PARAM = "taskParam";
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

    static SingUpCommand getInstance(RequestFactory requestFactory,
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
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String email = request.getParameter(EMAIL_REQUEST_PARAM_NAME);
        final String firstName = request.getParameter(FIRST_NAME_REQUEST_PARAM_NAME);
        final String secondName = request.getParameter(SECOND_NAME_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        final String repeatPassword = request.getParameter(REPEAT_PASSWORD_REQUEST_PARAM_NAME);

        Optional<String> exception = validate(login, email, password, repeatPassword);
        if (exception.isPresent()) {
            request.addAttributeToJsp(ERROR_REGISTRATION_ATTRIBUTE, exception.get());
            return requestFactory.createForwardResponse(propertyContext.get(SING_UP_PAGE));
        }
        return checkForTasks(request, accountService.add(createAccount(login, email, firstName, secondName, password)));
    }

    private Optional<String> validate(String login, String email, String password, String repeatPassword) {
        if (accountService.findByEmail(email).isPresent()){
            return Optional.of(EMAIL_BUSY_ERROR);
        } else if(accountService.findByLogin(login).isPresent()){
            return Optional.of(LOGIN_BUSY_ERROR);
        } else if(!password.equals(repeatPassword)){
            return Optional.of(PASSWORD_MISMATCH_ERROR);
        } else if(password.length() < MIN_PASSWORD_LENGTH){
            return Optional.of(PASSWORD_LENGTH_ERROR);
        } else {
            return Optional.empty();
        }
    }

    private Account createAccount(String login, String email, String firstName, String secondName, String password){
        final User user = userService.add(new User(firstName, secondName));
        return accountService.add(new Account(login, email, password, user, Role.ASPIRANT, AccountStatus.UNBANNED));
    }

    private CommandResponse checkForTasks(CommandRequest request, Account account){
        Optional<Object> possibleTask = request.retrieveFromSession(POSSIBLE_TASK_PARAM_NAME);
        Optional<Object> taskParam = request.retrieveFromSession(TASK_PARAM);
        request.createSession();
        request.addToSession(ACCOUNT_SESSION_ATTRIBUTE, account);
        if (possibleTask.isPresent() && taskParam.isPresent()) {
            request.addToSession(TASK_PARAM, taskParam.get());
            return requestFactory.createRedirectResponse((String) possibleTask.get());
        }
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }
}
