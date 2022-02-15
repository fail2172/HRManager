package com.epam.jwd.hrmanager.command.impl.action;

import com.epam.jwd.hrmanager.command.Authorized;
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

    public static final String COMMAND_JOB_REQUESTS_PAGE_PROPERTY = "command/job_requests_page";

    private static final String JOB_REQUEST_ID_PARAM = "jobRequestId";

    private static final ReentrantLock lock = new ReentrantLock();
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

    public static RejectJobRequestCommand getInstance(RequestFactory requestFactory, JobRequestService jobRequest,
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
    @Authorized
    public CommandResponse execute(CommandRequest request) {
        final Long jobRequestId = Long.parseLong(request.getParameter(JOB_REQUEST_ID_PARAM));
        final JobRequest oldJobRequest = jobRequestService.get(jobRequestId);
        jobRequestService.update(oldJobRequest.withStatus(JobRequestStatus.DENIED));
        return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_JOB_REQUESTS_PAGE_PROPERTY));
    }
}
