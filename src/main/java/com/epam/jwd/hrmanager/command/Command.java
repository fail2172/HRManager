package com.epam.jwd.hrmanager.command;

import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;

public interface Command {

    CommandResponse execute(CommandRequest request);

    static Command of(String name){
        return CommandRegistry.of(name);
    }

}
