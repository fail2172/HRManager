package com.epam.jwd.hrmanager.command.impl.page;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.Role;
import com.epam.jwd.hrmanager.secvice.AccountService;
import com.epam.jwd.hrmanager.secvice.UserService;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ShowUsersPageCommand implements Command {

    private static final String USER_PAGE = "page.users";

    private static final String USERS_ATTRIBUTE_NAME = "accounts";
    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowUsersPageCommand instance;

    private final RequestFactory requestFactory;
    private final AccountService accountService;
    private final PropertyContext propertyContext;

    private ShowUsersPageCommand(RequestFactory requestFactory, AccountService accountService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.accountService = accountService;
        this.propertyContext = propertyContext;
    }

    public static ShowUsersPageCommand getInstance(RequestFactory requestFactory,
                                                   AccountService accountService,
                                                   PropertyContext propertyContext){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new ShowUsersPageCommand(requestFactory, accountService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        request.addAttributeToJsp(
                USERS_ATTRIBUTE_NAME, accountService.findAll().stream()
                        .filter(a -> !a.getRole().equals(Role.ADMINISTRATOR))
                        .collect(Collectors.toList())
        );
        return requestFactory.createForwardResponse(propertyContext.get(USER_PAGE));
    }
}
