package com.epam.jwd.hrmanager.command.impl.action;

import com.epam.jwd.hrmanager.command.Authorized;
import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.secvice.JobRequestService;

import java.util.concurrent.locks.ReentrantLock;

public class GoToInterviewCreationPageCommand implements Command {

    private static final String SESSION_VARIABLE_PROPERTY = "session.variable";

    private static final String COMMAND_INTERVIEW_CREATION_PAGE_PROPERTY = "command/interview_creation_page";

    private static final String JOB_REQUEST_ID_PARAM = "jobRequestId";

    private static final ReentrantLock lock = new ReentrantLock();
    private static GoToInterviewCreationPageCommand instance;

    private final RequestFactory requestFactory;
    private final JobRequestService jobRequestService;
    private final PropertyContext propertyContext;

    private GoToInterviewCreationPageCommand(RequestFactory requestFactory, JobRequestService jobRequestService,
                                             PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.jobRequestService = jobRequestService;
        this.propertyContext = propertyContext;
    }

    public static GoToInterviewCreationPageCommand getInstance(RequestFactory requestFactory,
                                                               JobRequestService jobRequestService,
                                                               PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new GoToInterviewCreationPageCommand(requestFactory, jobRequestService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Authorized
    public CommandResponse execute(CommandRequest request) {
        final Long jobRequestId = Long.parseLong(request.getParameter(JOB_REQUEST_ID_PARAM));
        request.addToSession(propertyContext.get(SESSION_VARIABLE_PROPERTY), jobRequestService.get(jobRequestId));
        return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_INTERVIEW_CREATION_PAGE_PROPERTY));
    }
}
