package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.command.CommandCreationFactory;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.AccountService;
import com.epam.jwd.hrmanager.secvice.ServiceFactory;
import com.epam.jwd.hrmanager.secvice.UserService;

public class CommandCreationFactoryImpl implements CommandCreationFactory {

    private final RequestFactory requestFactory = RequestFactory.getInstance();
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();

    private CommandCreationFactoryImpl() {

    }

    public static CommandCreationFactoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Command createCommand(String name) {
        switch (name) {
            case "logout":
                return LogoutCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "show_error":
                return ShowErrorPageCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "login_page":
                return ShowLoginPageCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "user_page":
                return ShowUsersPage.getInstance(
                        requestFactory,
                        (UserService) serviceFactory.serviceFor(User.class),
                        PropertyContext.getInstance());
            case "login":
                return LoginCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        PropertyContext.getInstance()
                );
            default:
                return ShowMainPageCommand.getInstance(requestFactory, PropertyContext.getInstance());
        }
    }

    private static class Holder {
        private static final CommandCreationFactoryImpl INSTANCE = new CommandCreationFactoryImpl();
    }
}
