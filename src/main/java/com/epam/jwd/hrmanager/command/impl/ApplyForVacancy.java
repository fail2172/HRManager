package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.Vacancy;
import com.epam.jwd.hrmanager.model.JobRequest;
import com.epam.jwd.hrmanager.model.VacancyRequestStatus;
import com.epam.jwd.hrmanager.secvice.JobRequestService;
import com.epam.jwd.hrmanager.secvice.VacancyService;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ApplyForVacancy implements Command {

    public static final String TASK_PARAM_NAME = "task";
    private static final String INDEX_PAGE = "page.index";

    private static final String ACCOUNT_PARAM_NAME = "account";
    private static final String APPLY_FOR_VACANCY_COMMAND = "/controller?command=applyForVacancy";
    private static final String TASK_PARAM = "taskParam";
    private static final String APPLY_PARAM_NAME = "apply";
    private static final String SING_IN_COMMAND = "controller?command=singInPage";
    private static final ReentrantLock lock = new ReentrantLock();
    private static ApplyForVacancy instance;

    private final RequestFactory requestFactory;
    private final JobRequestService jobRequestService;
    private final VacancyService vacancyService;
    private final PropertyContext propertyContext;

    private ApplyForVacancy(RequestFactory requestFactory, JobRequestService jobRequestService,
                            VacancyService vacancyService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.jobRequestService = jobRequestService;
        this.vacancyService = vacancyService;
        this.propertyContext = propertyContext;
    }

    static ApplyForVacancy getInstance(RequestFactory requestFactory, JobRequestService jobRequestService,
                                       VacancyService vacancyService, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ApplyForVacancy(requestFactory, jobRequestService,
                            vacancyService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> checkAccount = request.retrieveFromSession(ACCOUNT_PARAM_NAME);
        if (!checkAccount.isPresent()) {
            return logIn(request);
        }
        final Vacancy vacancy = vacancyService.get(receiveVacancyId(request));
        final Account account = (Account) checkAccount.get();
        jobRequestService.add(new JobRequest(vacancy, account, VacancyRequestStatus.FIELD));
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }

    private CommandResponse logIn(CommandRequest request) {
        request.addToSession(TASK_PARAM_NAME, APPLY_FOR_VACANCY_COMMAND);
        request.addToSession(TASK_PARAM, Long.parseLong(request.getParameter(APPLY_PARAM_NAME)));
        return requestFactory.createRedirectResponse(SING_IN_COMMAND);
    }

    private Long receiveVacancyId(CommandRequest request){
        Optional<Object> taskParam = request.retrieveFromSession(TASK_PARAM);
        if (!taskParam.isPresent()) {
            return Long.parseLong(request.getParameter(APPLY_PARAM_NAME));
        } else {
            final Long vacancyId = (Long) taskParam.get();
            request.removeFromSession(TASK_PARAM);
            return vacancyId;
        }
    }
}
