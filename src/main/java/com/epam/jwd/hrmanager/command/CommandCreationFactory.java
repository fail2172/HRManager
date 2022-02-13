package com.epam.jwd.hrmanager.command;

import com.epam.jwd.hrmanager.command.impl.CommandCreationFactoryImpl;

public interface CommandCreationFactory {

    Command createCommand(String name);

    static CommandCreationFactory getInstance(){
        return CommandCreationFactoryImpl.getInstance();
    }

}
