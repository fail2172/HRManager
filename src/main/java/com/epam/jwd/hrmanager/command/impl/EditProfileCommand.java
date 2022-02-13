package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class EditProfileCommand implements Command {

    public static final String JOB_REQUEST_PARAM_NAME = "jobRequest";
    private static final String INTERVIEW_CREATION_PAGE = "page.interviewCreation";

    private static final String ERROR_FILLING_OUT_THE_FORM_PARAM = "errorFillingOutTheFormMessage";
    private static final String CITY_NAME_IS_EMPTY = "city name is empty";
    private static final String STREET_NAME_IS_EMPTY = "street name is empty";
    private static final String INCORRECT_HOSE_NUMBER = "incorrect hose number";
    private final static Logger LOGGER = LogManager.getLogger(SingInCommand.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static EditProfileCommand instance;

    private final RequestFactory requestFactory;
    private final AccountService accountService;
    private final UserService userService;
    private final PropertyContext propertyContext;

    public EditProfileCommand(RequestFactory requestFactory, AccountService accountService,
                              UserService userService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.accountService = accountService;
        this.userService = userService;
        this.propertyContext = propertyContext;
    }


    static EditProfileCommand getInstance(RequestFactory requestFactory, AccountService accountService,
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
    public CommandResponse execute(CommandRequest request) {
        final String login = request.getParameter("login");
        final String email = request.getParameter("email");
        final String firstName = request.getParameter("firstName");
        final String secondName = request.getParameter("secondName");

        Optional<String> exception = validate(login, email, firstName, secondName);
        if(exception.isPresent()){
            request.addAttributeToJsp(ERROR_FILLING_OUT_THE_FORM_PARAM, exception.get());
            requestFactory.createForwardResponse(propertyContext.get("page.editProfile"));
        }

        final User updateUser = userService.update(((Account) request.retrieveFromSession("sessionAccount").get()).getUser().withFirstName(firstName).withSecondName(secondName));
        final Account updateAccount = accountService.update(((Account) request.retrieveFromSession("sessionAccount").get()).withLogin(login).withEmail(email).withUser(updateUser));

        request.addToSession("sessionAccount", updateAccount);
        return requestFactory.createRedirectResponse("controller?command=personalAreaPage");
    }

    Optional<String> validate(String login, String email, String firstName, String secondName) {
        if (login == null || login.isEmpty()) {
            return Optional.of(CITY_NAME_IS_EMPTY);
        } else if (email == null || email.isEmpty()) {
            return Optional.of(STREET_NAME_IS_EMPTY);
        } else if (firstName == null || firstName.isEmpty()) {
            return Optional.of(INCORRECT_HOSE_NUMBER);
        }else if (secondName == null || secondName.isEmpty()) {
            return Optional.of(INCORRECT_HOSE_NUMBER);
        } else {
            return Optional.empty();
        }
    }
}
