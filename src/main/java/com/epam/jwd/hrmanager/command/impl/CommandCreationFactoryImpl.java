package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.command.CommandCreationFactory;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.*;
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
            case "showError":
                return ShowErrorPageCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "singInPage":
                return ShowSingInPageCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "singUpPage":
                return ShowSingUpPageCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "userPage":
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
            case "filterVacancies":
                return FilterVacanciesCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        PropertyContext.getInstance()
                );
            case "jobRequestsPage":
                return ShowJobRequestsPage.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        PropertyContext.getInstance()
                );
            case "rejectApplication":
                return RejectJobRequestCommand.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        PropertyContext.getInstance()
                );
            case "singUp":
                return SingUpCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        (UserService) serviceFactory.serviceFor(User.class),
                        PropertyContext.getInstance());
            case "applyForVacancy":
                return ApplyForVacancy.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        PropertyContext.getInstance()
                );
            case "searchVacancies":
                return SearchVacanciesCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        (CityService) serviceFactory.serviceFor(City.class),
                        (EmployerService) serviceFactory.serviceFor(Employer.class),
                        PropertyContext.getInstance()
                );
            default:
                return ShowMainPageCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        (CityService) serviceFactory.serviceFor(City.class),
                        PropertyContext.getInstance()
                );
        }
    }

    private static class Holder {
        private static final CommandCreationFactoryImpl INSTANCE = new CommandCreationFactoryImpl();
    }
}
