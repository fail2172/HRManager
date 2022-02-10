package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.command.CommandCreationFactory;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.City;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.model.Vacancy;
import com.epam.jwd.hrmanager.secvice.*;

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
            case "singOut":
                return SingOutCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "show_error":
                return ShowErrorPageCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "singIn_page":
                return ShowSingInPageCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "singUp_page":
                return ShowSingUpPageCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "user_page":
                return ShowUsersPage.getInstance(
                        requestFactory,
                        (UserService) serviceFactory.serviceFor(User.class),
                        PropertyContext.getInstance());
            case "singIn":
                return SingInCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        PropertyContext.getInstance()
                );
            case "singUp":
                return SingUpCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        (UserService) serviceFactory.serviceFor(User.class),
                        PropertyContext.getInstance());
            case "filterVacancies":
                return FilterVacanciesCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        PropertyContext.getInstance());
            default:
                return ShowMainPageCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        (CityService) serviceFactory.serviceFor(City.class),
                        PropertyContext.getInstance());
        }
    }

    private static class Holder {
        private static final CommandCreationFactoryImpl INSTANCE = new CommandCreationFactoryImpl();
    }
}
