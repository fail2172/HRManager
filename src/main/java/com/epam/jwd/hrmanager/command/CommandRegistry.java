package com.epam.jwd.hrmanager.command;

import com.epam.jwd.hrmanager.model.Role;

import java.util.Arrays;
import java.util.List;

public enum CommandRegistry {
    MAIN_PAGE(CommandCreationFactory.getInstance().createCommand("main_page"), "main_page"),
    USER_PAGE(CommandCreationFactory.getInstance().createCommand("user_page"), "user_page", Role.ADMINISTRATOR, Role.EMPLOYEE),
    LOGIN_PAGE(CommandCreationFactory.getInstance().createCommand("login_page"), "login_page"),
    LOGIN(CommandCreationFactory.getInstance().createCommand("login"), "login"),
    LOGOUT(CommandCreationFactory.getInstance().createCommand("logout"), "logout"),
    ERROR_PAGE(CommandCreationFactory.getInstance().createCommand("show_error"), "show_error"),
    DEFAULT(CommandCreationFactory.getInstance().createCommand("main_page"), "main_page");

    private final Command command;
    private final String path;
    private final List<Role> allowedRoles;

    CommandRegistry(Command command, String path, Role... roles) {
        this.command = command;
        this.path = path;
        allowedRoles = roles != null && roles.length > 0 ? Arrays.asList(roles) : Role.valuesAsList();
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
