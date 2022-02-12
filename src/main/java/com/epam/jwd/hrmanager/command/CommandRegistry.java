package com.epam.jwd.hrmanager.command;

import com.epam.jwd.hrmanager.model.Role;

import java.util.Arrays;
import java.util.List;

public enum CommandRegistry {
    USER_PAGE("userPage", Role.ADMINISTRATOR, Role.EMPLOYEE),
    JOB_REQUESTS_PAGE("jobRequestsPage", Role.ADMINISTRATOR, Role.EMPLOYEE),
    INTERVIEW_CREATION_PAGE("interviewCreationPage", Role.ADMINISTRATOR, Role.EMPLOYEE),
    GO_TO_INTERVIEW_CREATION_PAGE("goToInterviewCreationPage", Role.ADMINISTRATOR, Role.EMPLOYEE),
    REJECT_APPLICATION("rejectApplication", Role.ADMINISTRATOR, Role.EMPLOYEE),
    CREATE_AN_INTERVIEW("creteAnInterview"),
    MAIN_PAGE("mainPage"),
    SING_IN_PAGE("singInPage"),
    SING_UP_PAGE("singUpPage"),
    SING_IN("singIn"),
    SING_UP("singUp"),
    SING_OUT("singOut"),
    FILTER_VACANCIES("filterVacancies"),
    SEARCH_VACANCIES("searchVacancies"),
    APPLY_FOR_VACANCY("applyForVacancy"),
    ERROR_PAGE("showError"),
    DEFAULT("mainPage");

    private final List<Role> allowedRoles;
    private final Command command;
    private final String path;

    CommandRegistry(String path, Role... roles) {
        this.allowedRoles = roles != null && roles.length > 0 ? Arrays.asList(roles) : Role.valuesAsList();
        this.command = CommandCreationFactory.getInstance().createCommand(path);
        this.path = path;
    }

    public Command getCommand() {
        return command;
    }

    public List<Role> getAllowedRoles() {
        return allowedRoles;
    }

    public static Command of(String name) {
        for (CommandRegistry constanta : values()
        ) {
            if (constanta.path.equalsIgnoreCase(name)) {
                return constanta.command;
            }
        }
        return DEFAULT.command;
    }

}
