package com.epam.jwd.hrmanager.command;

import com.epam.jwd.hrmanager.model.Role;

import java.util.Arrays;
import java.util.List;

public enum CommandRegistry {
    VACANCY_CREATION("vacancyCreation", Role.ADMINISTRATOR),
    DELETE_VACANCY("deleteVacancy", Role.ADMINISTRATOR),
    USER_PAGE("usersPage", Role.ADMINISTRATOR),
    ASPIRANT_TO_MANAGER("aspirantToManager", Role.ADMINISTRATOR),
    BAN("ban", Role.ADMINISTRATOR),
    UNBAN("unBan", Role.ADMINISTRATOR),
    DELETE_ACCOUNT("deleteAccount", Role.ADMINISTRATOR),
    VACANCY_CREATION_PAGE("vacancyCreationPage", Role.ADMINISTRATOR),
    JOB_REQUESTS_PAGE("jobRequestsPage", Role.ADMINISTRATOR, Role.MANAGER),
    INTERVIEW_CREATION_PAGE("interviewCreationPage", Role.ADMINISTRATOR, Role.MANAGER),
    CREATE_INTERVIEW("creteInterview", Role.ADMINISTRATOR, Role.MANAGER),
    GO_TO_INTERVIEW_CREATION_PAGE("goToInterviewCreationPage", Role.ADMINISTRATOR, Role.MANAGER),
    REJECT_APPLICATION("rejectApplication", Role.ADMINISTRATOR, Role.MANAGER),
    MAIN_PAGE("mainPage"),
    SING_IN_PAGE("singInPage"),
    SING_UP_PAGE("singUpPage"),
    SING_IN("singIn"),
    SING_UP("singUp"),
    SING_OUT("singOut"),
    FILTER_VACANCIES("filterVacancies"),
    SEARCH_VACANCIES("searchVacancies"),
    APPLY_VACANCY("applyVacancy"),
    PERSONAL_AREA_PAGE("personalAreaPage"),
    EDIT_PROFILE_PAGE("editProfilePage"),
    EDIT_PROFILE("editProfile"),
    UNAUTHORIZED_ERROR("unauthorizedError"),
    NOT_FOUND_ERROR("notFoundError"),
    FORBIDDEN_ERROR("forbiddenError"),
    SERVER_ERROR("serverError"),
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
