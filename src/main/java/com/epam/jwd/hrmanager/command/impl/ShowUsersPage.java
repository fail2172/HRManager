package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.EntityService;
import com.epam.jwd.hrmanager.secvice.UserService;

import java.util.concurrent.locks.ReentrantLock;

public class ShowUsersPage implements Command {

    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String USER_JSP_PATH = "/WEB-INF/jsp/users.jsp";
    private static final ReentrantLock lock = new ReentrantLock();

    private static ShowUsersPage instance;
    private final RequestFactory requestFactory;
    private final UserService userService;

    private ShowUsersPage(RequestFactory requestFactory, UserService userService) {
        this.requestFactory = requestFactory;
        this.userService = userService;
    }

    static ShowUsersPage getInstance(RequestFactory requestFactory, UserService userService){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new ShowUsersPage(requestFactory, userService);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        request.addAttributeToJsp(USERS_ATTRIBUTE_NAME, userService.findAll());
        return requestFactory.createForwardResponse(USER_JSP_PATH);
    }
}
