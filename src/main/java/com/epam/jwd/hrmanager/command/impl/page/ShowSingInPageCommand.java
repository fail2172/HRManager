package com.epam.jwd.hrmanager.command.impl.page;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;

import java.util.concurrent.locks.ReentrantLock;

public class ShowSingInPageCommand implements Command {

    public static final String SING_IN_PAGE = "page.singIn";

    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowSingInPageCommand instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private ShowSingInPageCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowSingInPageCommand getInstance(RequestFactory requestFactory, PropertyContext propertyContext){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new ShowSingInPageCommand(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(SING_IN_PAGE));
    }
}
