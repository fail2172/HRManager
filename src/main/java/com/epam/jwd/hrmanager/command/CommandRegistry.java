package com.epam.jwd.hrmanager.command;

import com.epam.jwd.hrmanager.model.Role;

import java.util.Arrays;
import java.util.List;

public enum CommandRegistry {
    MAIN_PAGE("main_page"),
    USER_PAGE("user_page", Role.ADMINISTRATOR, Role.EMPLOYEE),
    SING_IN_PAGE("singIn_page"),
    SING_UP_PAGE("singUp_page"),
    SING_IN("singIn"),
    SING_UP("singUp"),
    SING_OUT("singOut"),
    FILTER_VACANCIES("filterVacancies"),
    ERROR_PAGE("show_error"),
    DEFAULT("main_page");

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
