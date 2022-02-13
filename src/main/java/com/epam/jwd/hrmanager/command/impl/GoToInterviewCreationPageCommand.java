package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.JobRequest;
import com.epam.jwd.hrmanager.secvice.JobRequestService;

import java.util.concurrent.locks.ReentrantLock;

public class GoToInterviewCreationPageCommand implements Command {

    private static final String SHOW_INTERVIEW_CREATION_PAGE_COMMAND = "/controller?command=interviewCreationPage";

    private static final String JOB_REQUEST_ID_PARAM_NAME = "jobRequestId";
    private static final String JOB_REQUEST_PARAM_NAME = "jobRequest";
    private static final ReentrantLock lock = new ReentrantLock();
    private static GoToInterviewCreationPageCommand instance;

    private final RequestFactory requestFactory;
    private final JobRequestService jobRequestService;

    private GoToInterviewCreationPageCommand(RequestFactory requestFactory, JobRequestService jobRequestService) {
        this.requestFactory = requestFactory;
        this.jobRequestService = jobRequestService;
    }

    static GoToInterviewCreationPageCommand getInstance(RequestFactory requestFactory,
                                                        JobRequestService jobRequestService) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new GoToInterviewCreationPageCommand(requestFactory, jobRequestService);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long jobRequestId = Long.parseLong(request.getParameter(JOB_REQUEST_ID_PARAM_NAME));
        request.addToSession(JOB_REQUEST_PARAM_NAME, jobRequestService.get(jobRequestId));
        return requestFactory.createRedirectResponse(SHOW_INTERVIEW_CREATION_PAGE_COMMAND);
    }
}
