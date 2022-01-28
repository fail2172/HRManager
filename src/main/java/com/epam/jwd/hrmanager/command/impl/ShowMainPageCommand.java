package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.command.CommandRequest;
import com.epam.jwd.hrmanager.command.CommandResponse;

public class ShowMainPageCommand implements Command {

    private ShowMainPageCommand(){

    }

    public static ShowMainPageCommand getInstance(){
        return Holder.INSTANCE;
    }

    private static final CommandResponse SHOW_MAIN_PAGE = new CommandResponse() {
        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return "/WEB-INF/jsp/main.jsp";
        }
    };

    @Override
    public CommandResponse execute(CommandRequest request) {
        return SHOW_MAIN_PAGE;
    }

    private static class Holder {
        private static final ShowMainPageCommand INSTANCE = new ShowMainPageCommand();
    }
}
