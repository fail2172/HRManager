package com.epam.jwd.hrmanager.command;

public interface Command {

    CommandResponse execute(CommandRequest request);

    static Command of(String name){
        return CommandRegistry.of(name);
    }

}
