package com.epam.jwd.hrmanager.command.impl.action;

import com.epam.jwd.hrmanager.command.Authorized;
import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.exception.FillingOutTheFormException;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class EditProfileCommand implements Command {

    private static final String SESSION_ERROR_MESSAGE_PROPERTY = "session.error_message";
    private static final String SESSION_ACCOUNT_PROPERTY = "session.account";

    private static final String COMMAND_EDIT_PROFILE_PAGE_PROPERTY = "command/edit_profile_page";
    private static final String COMMAND_PERSONAL_AREA_PAGE_PROPERTY = "command/personal_area_page";

    private static final String LOGIN_IS_EMPTY = "login is empty";
    private static final String EMAIL_IS_EMPTY = "email is empty";
    private static final String FIRST_NAME_IS_EMPTY = "first name is empty";
    private static final String SECOND_NAME_IS_EMPTY = "second name is empty";
    private static final String LOGIN_IS_BUSY = "login is busy";
    private static final String EMAIL_IS_BUSY = "email is busy";

    private static final String LOGIN = "login";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "firstName";
    private static final String SECOND_NAME = "secondName";

    private final static Logger LOGGER = LogManager.getLogger(EditProfileCommand.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static EditProfileCommand instance;

    private final RequestFactory requestFactory;
    private final AccountService accountService;
    private final UserService userService;
    private final PropertyContext propertyContext;

    public EditProfileCommand(RequestFactory requestFactory, AccountService accountService, UserService userService,
                              PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.accountService = accountService;
        this.userService = userService;
        this.propertyContext = propertyContext;
    }


    public static EditProfileCommand getInstance(RequestFactory requestFactory, AccountService accountService,
                                                 UserService userService, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new EditProfileCommand(requestFactory, accountService, userService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Authorized
    public CommandResponse execute(CommandRequest request) {
        try {
            updateAccount(request);
        } catch (FillingOutTheFormException e) {
            LOGGER.error(e);
            request.addToSession(propertyContext.get(SESSION_ERROR_MESSAGE_PROPERTY), e.getMessage());
            return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_EDIT_PROFILE_PAGE_PROPERTY));
        }
        return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_PERSONAL_AREA_PAGE_PROPERTY));
    }

    private void updateAccount(CommandRequest request) throws FillingOutTheFormException {
        final String login = request.getParameter(LOGIN);
        final String email = request.getParameter(EMAIL);
        final String firstName = request.getParameter(FIRST_NAME);
        final String secondName = request.getParameter(SECOND_NAME);

        validate(login, email, firstName, secondName);

        final Optional<Object> checkAccount = request.retrieveFromSession(propertyContext.get(SESSION_ACCOUNT_PROPERTY));
        if (checkAccount.isPresent()) {
            final Account account = (Account) checkAccount.get();
            final User updateUser = userService.update(account.getUser()
                    .withFirstName(firstName)
                    .withSecondName(secondName));
            final Account updateAccount = accountService.update(account
                    .withLogin(login)
                    .withEmail(email)
                    .withUser(updateUser));
            request.addToSession(propertyContext.get(SESSION_ACCOUNT_PROPERTY), updateAccount);
        }
    }

    private void validate(String login, String email, String firstName, String secondName) throws FillingOutTheFormException {
        if (login == null || login.isEmpty()) {
            throw new FillingOutTheFormException(LOGIN_IS_EMPTY);
        } else if (email == null || email.isEmpty()) {
            throw new FillingOutTheFormException(EMAIL_IS_EMPTY);
        } else if (firstName == null || firstName.isEmpty()) {
            throw new FillingOutTheFormException(FIRST_NAME_IS_EMPTY);
        } else if (secondName == null || secondName.isEmpty()) {
            throw new FillingOutTheFormException(SECOND_NAME_IS_EMPTY);
        } else if (accountService.findByLogin(login).isPresent()) {
            throw new FillingOutTheFormException(LOGIN_IS_BUSY);
        } else if (accountService.findByEmail(email).isPresent()) {
            throw new FillingOutTheFormException(EMAIL_IS_BUSY);
        }
    }
}
