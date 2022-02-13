package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.secvice.AccountService;
import com.epam.jwd.hrmanager.secvice.InterviewService;
import com.epam.jwd.hrmanager.secvice.JobRequestService;

import java.util.concurrent.locks.ReentrantLock;

public class DeleteAccountCommand implements Command {

    private static final String INDEX_PAGE = "page.index";

    private static final String ACCOUNT_SESSION_ATTRIBUTE = "sessionAccount";
    private static final ReentrantLock lock = new ReentrantLock();
    private static DeleteAccountCommand instance;

    private final RequestFactory requestFactory;
    private final AccountService accountService;
    private final InterviewService interviewService;
    private final JobRequestService jobRequestService;
    private final PropertyContext propertyContext;

    private DeleteAccountCommand(RequestFactory requestFactory, AccountService accountService,
                                 InterviewService interviewService, JobRequestService jobRequestService,
                                 PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.accountService = accountService;
        this.interviewService = interviewService;
        this.jobRequestService = jobRequestService;
        this.propertyContext = propertyContext;
    }

    static DeleteAccountCommand getInstance(RequestFactory requestFactory, AccountService accountService,
                                            InterviewService interviewService, JobRequestService jobRequestService,
                                            PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new DeleteAccountCommand(requestFactory, accountService, interviewService,
                            jobRequestService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Account account = accountService.get(Long.parseLong(request.getParameter("accountId")));
        interviewService.findByUser(account.getUser()).forEach(i -> interviewService.delete(i.getId()));
        jobRequestService.findByAccount(account).forEach(jr -> jobRequestService.delete(jr.getId()));
        accountService.delete(account.getId());
        return requestFactory.createRedirectResponse("/controller?command=usersPage");
    }
}
