package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.JobRequest;
import com.epam.jwd.hrmanager.model.JobRequestStatus;
import com.epam.jwd.hrmanager.secvice.JobRequestService;

import java.util.concurrent.locks.ReentrantLock;

public class RejectJobRequestCommand implements Command {

    private static final String JOB_REQUESTS_PAGE = "page.jobRequests";

    private static final String JOB_REQUEST_ID_PARAM_NAME = "jobRequestId";
    private static final ReentrantLock lock = new ReentrantLock();
    public static final String JOB_REQUESTS_PAGE_COMMAND = "/controller?command=jobRequestsPage";
    private static RejectJobRequestCommand instance;

    private final RequestFactory requestFactory;
    private final JobRequestService jobRequestService;
    private final PropertyContext propertyContext;

    private RejectJobRequestCommand(RequestFactory requestFactory, JobRequestService jobRequestService,
                                    PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.jobRequestService = jobRequestService;
        this.propertyContext = propertyContext;
    }

    static RejectJobRequestCommand getInstance(RequestFactory requestFactory, JobRequestService jobRequest,
                                               PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new RejectJobRequestCommand(requestFactory, jobRequest, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Long jobRequestId = Long.parseLong(request.getParameter(JOB_REQUEST_ID_PARAM_NAME));
        JobRequest oldJobRequest = jobRequestService.get(jobRequestId);
        jobRequestService.update(oldJobRequest.withStatus(JobRequestStatus.DENIED));
        return requestFactory.createRedirectResponse(JOB_REQUESTS_PAGE_COMMAND);
    }
}
