package com.epam.jwd.hrmanager.command.impl.page;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ShowInterviewCreationPageCommand implements Command {

    private static final String INTERVIEW_CREATION_PAGE = "page.interviewCreation";

    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowInterviewCreationPageCommand instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private ShowInterviewCreationPageCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowInterviewCreationPageCommand getInstance(RequestFactory requestFactory, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ShowInterviewCreationPageCommand(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(INTERVIEW_CREATION_PAGE));
    }
}
