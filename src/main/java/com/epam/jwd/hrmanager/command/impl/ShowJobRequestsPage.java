package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.JobRequest;
import com.epam.jwd.hrmanager.model.VacancyRequestStatus;
import com.epam.jwd.hrmanager.secvice.JobRequestService;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ShowJobRequestsPage implements Command {

    private static final String JOB_REQUESTS_PAGE = "page.jobRequests";

    private static final String VACANCIES_REQUEST_ATTRIBUTE_NAME = "jobInquiries";
    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowJobRequestsPage instance;

    private final RequestFactory requestFactory;
    private final JobRequestService jobRequestService;
    private final PropertyContext propertyContext;

    private ShowJobRequestsPage(RequestFactory requestFactory, JobRequestService jobRequestService,
                                PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.jobRequestService = jobRequestService;
        this.propertyContext = propertyContext;
    }

    static ShowJobRequestsPage getInstance(RequestFactory requestFactory, JobRequestService jobRequestService,
                                           PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ShowJobRequestsPage(requestFactory, jobRequestService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<JobRequest> jobRequests = jobRequestService.findAll().stream()
                .filter(vacancyRequest -> vacancyRequest.getStatus().equals(VacancyRequestStatus.FIELD))
                .collect(Collectors.toList());
        request.addAttributeToJsp(VACANCIES_REQUEST_ATTRIBUTE_NAME, jobRequests);
        return requestFactory.createForwardResponse(propertyContext.get(JOB_REQUESTS_PAGE));
    }
}
