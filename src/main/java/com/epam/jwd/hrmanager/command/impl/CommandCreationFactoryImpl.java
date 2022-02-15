package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.command.CommandCreationFactory;
import com.epam.jwd.hrmanager.command.ProxyWithAuthorization;
import com.epam.jwd.hrmanager.command.impl.action.*;
import com.epam.jwd.hrmanager.command.impl.error.ForbiddenErrorPage;
import com.epam.jwd.hrmanager.command.impl.error.NotFoundErrorPage;
import com.epam.jwd.hrmanager.command.impl.error.ServerErrorPage;
import com.epam.jwd.hrmanager.command.impl.error.UnauthorizedErrorPage;
import com.epam.jwd.hrmanager.command.impl.page.*;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.*;

public class CommandCreationFactoryImpl implements CommandCreationFactory {

    private final RequestFactory requestFactory = RequestFactory.getInstance();
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private final PropertyContext propertyContext = PropertyContext.getInstance();

    private CommandCreationFactoryImpl() {

    }

    public static CommandCreationFactoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Command createCommand(String name) {
        return withAuthorization(commandWithoutAuthorizationAnnotation(name));
    }

    private Command commandWithoutAuthorizationAnnotation(String name) {
        switch (name) {
            case "singOut":
                return SingOutCommand.getInstance(requestFactory, propertyContext);
            case "unauthorizedError":
                return UnauthorizedErrorPage.getInstance(requestFactory, propertyContext);
            case "forbiddenError":
                return ForbiddenErrorPage.getInstance(requestFactory, propertyContext);
            case "serverError":
                return ServerErrorPage.getInstance(requestFactory, propertyContext);
            case "singInPage":
                return ShowSingInPageCommand.getInstance(requestFactory, propertyContext);
            case "singUpPage":
                return ShowSingUpPageCommand.getInstance(requestFactory, propertyContext);
            case "interviewCreationPage":
                return ShowInterviewCreationPageCommand.getInstance(requestFactory, propertyContext);
            case "editProfilePage":
                return ShowEditProfilePageCommand.getInstance(requestFactory, propertyContext);
            case "vacancyCreationPage":
                return ShowVacancyCreationCommand.getInstance(requestFactory, propertyContext);
            case "goToInterviewCreationPage":
                return GoToInterviewCreationPageCommand.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        propertyContext
                );
            case "ban":
                return BanCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        propertyContext
                );
            case "unBan":
                return UnBanCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        propertyContext
                );
            case "aspirantToManager":
                return AspirantToManagerCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        propertyContext
                );
            case "usersPage":
                return ShowUsersPageCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        propertyContext
                );
            case "singIn":
                return SingInCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        propertyContext
                );
            case "filterVacancies":
                return FilterVacanciesCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        propertyContext
                );
            case "jobRequestsPage":
                return ShowJobRequestsPageCommand.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        propertyContext
                );
            case "rejectApplication":
                return RejectJobRequestCommand.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        propertyContext
                );
            case "singUp":
                return SingUpCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        (UserService) serviceFactory.serviceFor(User.class),
                        propertyContext
                );
            case "applyVacancy":
                return ApplyVacancy.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        propertyContext
                );
            case "editProfile":
                return EditProfileCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        (UserService) serviceFactory.serviceFor(User.class),
                        propertyContext
                );
            case "mainPage":
                return ShowMainPageCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        (CityService) serviceFactory.serviceFor(City.class),
                        propertyContext
                );
            case "personalAreaPage":
                return ShowPersonalAreaPageCommand.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        (InterviewService) serviceFactory.serviceFor(Interview.class),
                        propertyContext
                );
            case "deleteAccount":
                return DeleteAccountCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        (InterviewService) serviceFactory.serviceFor(Interview.class),
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        propertyContext
                );
            case "searchVacancies":
                return SearchVacanciesCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        (CityService) serviceFactory.serviceFor(City.class),
                        (EmployerService) serviceFactory.serviceFor(Employer.class),
                        propertyContext
                );
            case "deleteVacancy":
                return DeleteVacancyCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        (InterviewService) serviceFactory.serviceFor(Interview.class),
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        propertyContext
                );
            case "vacancyCreation":
                return CreationVacancyCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        (EmployerService) serviceFactory.serviceFor(Employer.class),
                        (CityService) serviceFactory.serviceFor(City.class),
                        propertyContext
                );
            case "creteInterview":
                return CreatingInterviewCommand.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        (InterviewService) serviceFactory.serviceFor(Interview.class),
                        (CityService) serviceFactory.serviceFor(City.class),
                        (StreetService) serviceFactory.serviceFor(Street.class),
                        (AddressService) serviceFactory.serviceFor(Address.class),
                        propertyContext
                );
            default:
                return NotFoundErrorPage.getInstance(requestFactory, propertyContext);
        }
    }

    private Command withAuthorization(Command command) {
        return ProxyWithAuthorization.of(command);
    }

    private static class Holder {
        private static final CommandCreationFactoryImpl INSTANCE = new CommandCreationFactoryImpl();
    }
}
