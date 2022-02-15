package com.epam.jwd.hrmanager.command.impl.action;

import com.epam.jwd.hrmanager.command.Authorized;
import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.exception.FillingOutTheFormException;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.concurrent.locks.ReentrantLock;

public class CreationVacancyCommand implements Command {

    private static final String SESSION_ERROR_MESSAGE_PROPERTY = "session.error_message";

    private static final String COMMAND_VACANCY_CREATION_PAGE_PROPERTY = "command/vacancy_creation_page";
    private static final String COMMAND_MAIN_PAGE_PROPERTY = "command/main_page";

    private static final String CITY_NAME_IS_EMPTY = "city name is empty";
    private static final String TITLE_IS_EMPTY = "title is empty";
    private static final String EMPLOYER_NAME_IS_EMPTY = "employer name is empty";
    private static final String INCORRECT_SALARY = "incorrect salary";
    private static final String INCORRECT_EMPLOYMENT = "incorrect employment";

    private static final String TITLE = "title";
    private static final String EMPLOYER = "employer";
    private static final String CITY = "city";
    private static final String SALARY = "salary";
    private static final String EMPLOYMENT_TYPE = "employmentType";
    private static final String DESCRIPTION = "description";
    private static final String EXPERIENCE = "experience";
    private static final String NULL = "null";
    private static final String EMPTY_STRING = "";
    private static final int ZERO = 0;

    private final static Logger LOGGER = LogManager.getLogger(SingInCommand.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static CreationVacancyCommand instance;

    private final RequestFactory requestFactory;
    private final VacancyService vacancyService;
    private final EmployerService employerService;
    private final CityService cityService;
    private final PropertyContext propertyContext;

    public CreationVacancyCommand(RequestFactory requestFactory, VacancyService vacancyService,
                                  EmployerService employerService, CityService cityService,
                                  PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.vacancyService = vacancyService;
        this.employerService = employerService;
        this.cityService = cityService;
        this.propertyContext = propertyContext;
    }

    public static CreationVacancyCommand getInstance(RequestFactory requestFactory, VacancyService vacancyService,
                                                     EmployerService employerService, CityService cityService,
                                                     PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new CreationVacancyCommand(requestFactory, vacancyService, employerService,
                            cityService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Authorized
    public CommandResponse execute(CommandRequest request) {
        try {
            createVacancy(request);
        } catch (FillingOutTheFormException e) {
            LOGGER.error(e);
            request.addToSession(propertyContext.get(SESSION_ERROR_MESSAGE_PROPERTY), e.getMessage());
            return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_VACANCY_CREATION_PAGE_PROPERTY));
        }
        return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_MAIN_PAGE_PROPERTY));
    }

    private void createVacancy(CommandRequest request) throws FillingOutTheFormException {
        final String title = request.getParameter(TITLE);
        final String employerName = request.getParameter(EMPLOYER);
        final String cityName = request.getParameter(CITY);
        final String salary = request.getParameter(SALARY);
        final String employment = request.getParameter(EMPLOYMENT_TYPE);
        final String description = request.getParameter(DESCRIPTION);
        final int experience = receiveExperience(request);

        validate(title, employerName, cityName, salary, employment);

        final Employer employer = employerService.add(new Employer(employerName, EMPTY_STRING));
        final City city = cityService.add(new City(cityName));
        vacancyService.add(new Vacancy(title, new BigDecimal(salary), employer, city, Employment.of(employment),
                experience, new Date(System.currentTimeMillis()), description));
    }

    private void validate(String title, String employer, String city, String salary, String employment)
            throws FillingOutTheFormException {
        if (title == null || title.isEmpty()) {
            throw new FillingOutTheFormException(TITLE_IS_EMPTY);
        } else if (employer == null || employer.isEmpty()) {
            throw new FillingOutTheFormException(EMPLOYER_NAME_IS_EMPTY);
        } else if (city == null || city.isEmpty()) {
            throw new FillingOutTheFormException(CITY_NAME_IS_EMPTY);
        } else if (salary == null || salary.isEmpty()) {
            throw new FillingOutTheFormException(INCORRECT_SALARY);
        } else if (employment == null || employment.equals(NULL)) {
            throw new FillingOutTheFormException(INCORRECT_EMPLOYMENT);
        }
    }

    private int receiveExperience(CommandRequest request) {
        final String experience = request.getParameter(EXPERIENCE);
        if (experience == null || experience.isEmpty()) {
            return ZERO;
        } else {
            return Integer.parseInt(experience);
        }
    }
}
