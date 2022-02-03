package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ShowErrorPageCommand implements Command {

    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowErrorPageCommand instance;

    private final RequestFactory requestFactory;

    private ShowErrorPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    static ShowErrorPageCommand getInstance(RequestFactory requestFactory){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new ShowErrorPageCommand(requestFactory);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse("WEB-INF/jsp/error.jsp");
    }
}
