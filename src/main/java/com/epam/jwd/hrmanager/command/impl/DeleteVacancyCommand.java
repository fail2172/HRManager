package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.Vacancy;
import com.epam.jwd.hrmanager.secvice.*;

import java.util.concurrent.locks.ReentrantLock;

public class DeleteVacancyCommand implements Command {

    public static final String JOB_REQUEST_PARAM_NAME = "jobRequest";
    private static final String VACANCY_CREATION_PAGE = "page.vacancyCreation";

    private static final String ERROR_FILLING_OUT_THE_FORM_PARAM = "errorFillingOutTheFormMessage";
    private static final String CITY_NAME_IS_EMPTY = "city name is empty";
    private static final String STREET_NAME_IS_EMPTY = "street name is empty";
    private static final String INCORRECT_HOSE_NUMBER = "incorrect hose number";
    private static final ReentrantLock lock = new ReentrantLock();
    private static DeleteVacancyCommand instance;

    private final RequestFactory requestFactory;
    private final VacancyService vacancyService;
    private final InterviewService interviewService;
    private final JobRequestService jobRequestService;
    private final PropertyContext propertyContext;

    public DeleteVacancyCommand(RequestFactory requestFactory, VacancyService vacancyService,
                                InterviewService interviewService, JobRequestService jobRequestService,
                                PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.vacancyService = vacancyService;
        this.interviewService = interviewService;
        this.jobRequestService = jobRequestService;
        this.propertyContext = propertyContext;
    }


    static DeleteVacancyCommand getInstance(RequestFactory requestFactory, VacancyService vacancyService,
                                            InterviewService interviewService, JobRequestService jobRequestService,
                                            PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new DeleteVacancyCommand(requestFactory, vacancyService, interviewService,
                            jobRequestService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Vacancy vacancy = vacancyService.get(Long.parseLong(request.getParameter("vacancyId")));
        interviewService.findByVacancy(vacancy).forEach(i -> interviewService.delete(i.getId()));
        jobRequestService.findByVacancy(vacancy).forEach(jr -> jobRequestService.delete(jr.getId()));
        vacancyService.delete(vacancy.getId());
        return requestFactory.createRedirectResponse("/");
    }
}
