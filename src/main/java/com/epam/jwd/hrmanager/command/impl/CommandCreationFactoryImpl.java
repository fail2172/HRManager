package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.command.CommandCreationFactory;
import com.epam.jwd.hrmanager.command.impl.page.*;
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
            case "interviewCreationPage":
                return ShowInterviewCreationPageCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "editProfilePage":
                return ShowEditProfilePageCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "vacancyCreationPage":
                return ShowVacancyCreationCommand.getInstance(requestFactory, PropertyContext.getInstance());
            case "goToInterviewCreationPage":
                return GoToInterviewCreationPageCommand.getInstance(requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class));
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
                return ShowJobRequestsPageCommand.getInstance(
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
            case "editProfile":
                return EditProfileCommand.getInstance(
                        requestFactory,
                        (AccountService) serviceFactory.serviceFor(Account.class),
                        (UserService) serviceFactory.serviceFor(User.class),
                        PropertyContext.getInstance()
                );
            case "deleteVacancy":
                return DeleteVacancyCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        (InterviewService) serviceFactory.serviceFor(Interview.class),
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        PropertyContext.getInstance()
                );
            case "vacancyCreation":
                return CreationVacancyCommand.getInstance(
                        requestFactory,
                        (VacancyService) serviceFactory.serviceFor(Vacancy.class),
                        (EmployerService) serviceFactory.serviceFor(Employer.class),
                        (CityService) serviceFactory.serviceFor(City.class),
                        PropertyContext.getInstance()
                );
            case "creteAnInterview":
                return CreatingInterviewCommand.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        (InterviewService) serviceFactory.serviceFor(Interview.class),
                        (CityService) serviceFactory.serviceFor(City.class),
                        (StreetService) serviceFactory.serviceFor(Street.class),
                        (AddressService) serviceFactory.serviceFor(Address.class),
                        PropertyContext.getInstance()
                );
            case "personalAreaPage":
                return ShowPersonalAreaPageCommand.getInstance(
                        requestFactory,
                        (JobRequestService) serviceFactory.serviceFor(JobRequest.class),
                        (InterviewService) serviceFactory.serviceFor(Interview.class),
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
