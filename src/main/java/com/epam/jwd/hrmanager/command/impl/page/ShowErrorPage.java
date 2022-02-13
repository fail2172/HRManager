package com.epam.jwd.hrmanager.command.impl.page;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ShowErrorPage implements Command {

    private static final String INDEX_PAGE = "page.index";

    private static final String ACCOUNT_SESSION_ATTRIBUTE = "sessionAccount";
    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowErrorPage instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private ShowErrorPage(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    static ShowErrorPage getInstance(RequestFactory requestFactory, PropertyContext propertyContext){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new ShowErrorPage(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get("page.error"));
    }
}
