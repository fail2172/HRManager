package com.epam.jwd.hrmanager.command.impl.page;

import com.epam.jwd.hrmanager.command.Authorized;
import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ShowEditProfilePageCommand implements Command {

    private static final String INTERVIEW_CREATION_PAGE_PROPERTY = "page.editProfile";

    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowEditProfilePageCommand instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private ShowEditProfilePageCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowEditProfilePageCommand getInstance(RequestFactory requestFactory, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ShowEditProfilePageCommand(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Authorized
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(INTERVIEW_CREATION_PAGE_PROPERTY));
    }
}
