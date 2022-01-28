package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.command.CommandRequest;
import com.epam.jwd.hrmanager.command.CommandResponse;
import com.epam.jwd.hrmanager.model.Role;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.EntityService;
import com.epam.jwd.hrmanager.secvice.ServiceFactory;

import java.util.Arrays;
import java.util.List;

public class ShowUsersPage implements Command {

    private static final CommandResponse SHOW_USERS = new CommandResponse() {
        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return "/WEB-INF/jsp/users.jsp";
        }
    };

    private ShowUsersPage(){

    }

    public static ShowUsersPage getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
//        EntityService<User> userService = ServiceFactory.getInstance().serviceFor(User.class);
//        List<User> usersList = userService.findAll();
//        request.addAttributeToJsp("users", usersList);

        List<User> users = Arrays.asList(new User(Role.ASPIRANT, "Artsiom", "Salauyou"),
                new User(Role.EMPLOYEE, "Bob", "Jonson"));
        request.addAttributeToJsp("users", users);
        return SHOW_USERS;
    }

    private static class Holder {
        private static final ShowUsersPage INSTANCE = new ShowUsersPage();
    }
}
