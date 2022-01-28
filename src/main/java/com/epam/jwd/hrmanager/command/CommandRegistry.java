package com.epam.jwd.hrmanager.command;

import com.epam.jwd.hrmanager.command.impl.ShowMainPageCommand;
import com.epam.jwd.hrmanager.command.impl.ShowUsersPage;

public enum CommandRegistry {
    MAIN_PAGE(ShowMainPageCommand.getInstance()),
    USER_PAGE(ShowUsersPage.getInstance()),
    DEFAULT(ShowMainPageCommand.getInstance());

    private final Command command;

    CommandRegistry(Command command) {
        this.command = command;
    }

    static Command of(String name){
        for (CommandRegistry constanta : values()
             ) {
            if(constanta.name().equalsIgnoreCase(name)){
                return constanta.command;
            }
        }
        return DEFAULT.command;
    }

}
