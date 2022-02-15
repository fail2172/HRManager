package com.epam.jwd.hrmanager.command.impl.action;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.Vacancy;
import com.epam.jwd.hrmanager.model.JobRequest;
import com.epam.jwd.hrmanager.model.JobRequestStatus;
import com.epam.jwd.hrmanager.secvice.JobRequestService;
import com.epam.jwd.hrmanager.secvice.VacancyService;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ApplyVacancy implements Command {

    private static final String SESSION_TASK_PARAM_PROPERTY = "session.task_param";
    private static final String SESSION_ACCOUNT_PROPERTY = "session.account";
    private static final String SESSION_TASK_PROPERTY = "session.task";

    private static final String COMMAND_APPLY_VACANCY_PROPERTY = "command/apply_vacancy";
    private static final String SING_IN_PAGE_PROPERTY = "command/sing_in_page";
    private static final String COMMAND_MAIN_PAGE_PROPERTY = "command/main_page";

    private static final String VACANCY_ID_PARAM = "vacancyId";
    private static final ReentrantLock lock = new ReentrantLock();
    private static ApplyVacancy instance;

    private final RequestFactory requestFactory;
    private final JobRequestService jobRequestService;
    private final VacancyService vacancyService;
    private final PropertyContext propertyContext;

    private ApplyVacancy(RequestFactory requestFactory, JobRequestService jobRequestService,
                         VacancyService vacancyService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.jobRequestService = jobRequestService;
        this.vacancyService = vacancyService;
        this.propertyContext = propertyContext;
    }

    public static ApplyVacancy getInstance(RequestFactory requestFactory, JobRequestService jobRequestService,
                                           VacancyService vacancyService, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ApplyVacancy(requestFactory, jobRequestService,
                            vacancyService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> checkAccount = request.retrieveFromSession(propertyContext.get(SESSION_ACCOUNT_PROPERTY));
        if (!checkAccount.isPresent()) {
            return logIn(request);
        }
        final Vacancy vacancy = vacancyService.get(receiveVacancyId(request));
        final Account account = (Account) checkAccount.get();
        jobRequestService.add(new JobRequest(vacancy, account, JobRequestStatus.FIELD));
        return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_MAIN_PAGE_PROPERTY));
    }

    private CommandResponse logIn(CommandRequest request) {
        request.addToSession(
                propertyContext.get(SESSION_TASK_PROPERTY),
                propertyContext.get(COMMAND_APPLY_VACANCY_PROPERTY)
        );
        request.addToSession(
                propertyContext.get(SESSION_TASK_PARAM_PROPERTY),
                Long.parseLong(request.getParameter(VACANCY_ID_PARAM))
        );
        return requestFactory.createRedirectResponse(propertyContext.get(SING_IN_PAGE_PROPERTY));
    }

    private Long receiveVacancyId(CommandRequest request) {
        final Optional<Object> taskParam = request.retrieveFromSession(propertyContext.get(SESSION_TASK_PROPERTY));
        if (!taskParam.isPresent()) {
            return Long.parseLong(request.getParameter(VACANCY_ID_PARAM));
        } else {
            final Long vacancyId = (Long) taskParam.get();
            request.removeFromSession(propertyContext.get(SESSION_TASK_PARAM_PROPERTY));
            return vacancyId;
        }
    }
}
