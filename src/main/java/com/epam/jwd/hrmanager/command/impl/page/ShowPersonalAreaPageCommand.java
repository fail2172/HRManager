package com.epam.jwd.hrmanager.command.impl.page;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.Interview;
import com.epam.jwd.hrmanager.model.JobRequest;
import com.epam.jwd.hrmanager.secvice.InterviewService;
import com.epam.jwd.hrmanager.secvice.JobRequestService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ShowPersonalAreaPageCommand implements Command {

    private static final String PERSONAL_AREA_PAGE = "page.personalArea";

    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowPersonalAreaPageCommand instance;

    private final RequestFactory requestFactory;
    private final JobRequestService jobRequestService;
    private final InterviewService interviewService;
    private final PropertyContext propertyContext;

    private ShowPersonalAreaPageCommand(RequestFactory requestFactory, JobRequestService jobRequestService,
                                        InterviewService interviewService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.jobRequestService = jobRequestService;
        this.interviewService = interviewService;
        this.propertyContext = propertyContext;
    }

    public static ShowPersonalAreaPageCommand getInstance(RequestFactory requestFactory, JobRequestService jobRequestService,
                                                          InterviewService interviewService, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ShowPersonalAreaPageCommand(requestFactory, jobRequestService,
                            interviewService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Object> account = request.retrieveFromSession("sessionAccount");
        if (account.isPresent()) {
            addAttributes(request, (Account) account.get());
            return requestFactory.createForwardResponse(propertyContext.get(PERSONAL_AREA_PAGE));
        }

        return requestFactory.createForwardResponse(propertyContext.get(PERSONAL_AREA_PAGE));
    }

    private void addAttributes(CommandRequest request, Account account){
        List<JobRequest> jobRequests = jobRequestService.findByAccount(account);
        List<Interview> interviews = interviewService.findByUser(account.getUser());
        request.addAttributeToJsp("jobRequests", jobRequests);
        request.addAttributeToJsp("interviews", interviews);
    }
}
