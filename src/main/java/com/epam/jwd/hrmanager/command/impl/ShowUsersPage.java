package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.secvice.UserService;

import java.util.concurrent.locks.ReentrantLock;

public class ShowUsersPage implements Command {

    private static final String USER_PAGE = "page.users";

    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowUsersPage instance;

    private final RequestFactory requestFactory;
    private final UserService userService;
    private final PropertyContext propertyContext;

    private ShowUsersPage(RequestFactory requestFactory, UserService userService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.userService = userService;
        this.propertyContext = propertyContext;
    }

    static ShowUsersPage getInstance(RequestFactory requestFactory,
                                     UserService userService,
                                     PropertyContext propertyContext){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new ShowUsersPage(requestFactory, userService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        request.addAttributeToJsp(USERS_ATTRIBUTE_NAME, userService.findAll());
        return requestFactory.createForwardResponse(propertyContext.get(USER_PAGE));
    }
}
