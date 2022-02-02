package com.epam.jwd.hrmanager.command;

public enum CommandRegistry {
    MAIN_PAGE(CommandCreationFactory.getInstance().createCommand("main_page"), "main_page"),
    USER_PAGE(CommandCreationFactory.getInstance().createCommand("user_page"), "user_page"),
    LOGIN_PAGE(CommandCreationFactory.getInstance().createCommand("login_page"), "login_page"),
    LOGIN(CommandCreationFactory.getInstance().createCommand("login"), "login"),
    LOGOUT(CommandCreationFactory.getInstance().createCommand("logout"), "logout"),
    DEFAULT(CommandCreationFactory.getInstance().createCommand("main_page"), "main_page");

    private final Command command;
    private final String path;

    CommandRegistry(Command command, String path) {
        this.command = command;
        this.path = path;
    }

    static Command of(String name){
        for (CommandRegistry constanta : values()
             ) {
            if(constanta.path.equalsIgnoreCase(name)){
                return constanta.command;
            }
        }
        return DEFAULT.command;
    }

}
