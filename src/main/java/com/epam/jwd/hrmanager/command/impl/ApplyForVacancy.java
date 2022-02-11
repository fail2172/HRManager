package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.Vacancy;
import com.epam.jwd.hrmanager.model.VacancyRequest;
import com.epam.jwd.hrmanager.model.VacancyRequestStatus;
import com.epam.jwd.hrmanager.secvice.VacancyRequestService;
import com.epam.jwd.hrmanager.secvice.VacancyService;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ApplyForVacancy implements Command {

    private static final String SING_IN_PAGE = "page.singIn";
    private static final String INDEX_PAGE = "page.index";

    private static final ReentrantLock lock = new ReentrantLock();
    private static ApplyForVacancy instance;

    private final RequestFactory requestFactory;
    private final VacancyRequestService vacancyRequestService;
    private final VacancyService vacancyService;
    private final PropertyContext propertyContext;

    private ApplyForVacancy(RequestFactory requestFactory, VacancyRequestService vacancyRequestService,
                            VacancyService vacancyService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.vacancyRequestService = vacancyRequestService;
        this.vacancyService = vacancyService;
        this.propertyContext = propertyContext;
    }

    static ApplyForVacancy getInstance(RequestFactory requestFactory, VacancyRequestService vacancyRequestService,
                                       VacancyService vacancyService, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ApplyForVacancy(requestFactory, vacancyRequestService,
                            vacancyService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> checkAccount = request.retrieveFromSession("account");
        if (!checkAccount.isPresent()) {
            return requestFactory.createForwardResponse(propertyContext.get(SING_IN_PAGE));
        }
        Long vacancyId = Long.parseLong(request.getParameter("apply"));
        final Vacancy vacancy = vacancyService.get(vacancyId);
        final Account account = (Account) checkAccount.get();
        vacancyRequestService.add(new VacancyRequest(vacancy, account, VacancyRequestStatus.FIELD));
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }
}
