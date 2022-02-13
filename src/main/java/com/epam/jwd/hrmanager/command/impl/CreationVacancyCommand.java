package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class CreationVacancyCommand implements Command {

    public static final String JOB_REQUEST_PARAM_NAME = "jobRequest";
    private static final String VACANCY_CREATION_PAGE = "page.vacancyCreation";

    private static final String ERROR_FILLING_OUT_THE_FORM_PARAM = "errorFillingOutTheFormMessage";
    private static final String CITY_NAME_IS_EMPTY = "city name is empty";
    private static final String STREET_NAME_IS_EMPTY = "street name is empty";
    private static final String INCORRECT_HOSE_NUMBER = "incorrect hose number";
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

    static CreationVacancyCommand getInstance(RequestFactory requestFactory, VacancyService vacancyService,
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
    public CommandResponse execute(CommandRequest request) {
        final String title = request.getParameter("title");
        final String employerName = request.getParameter("employer");
        final String cityName = request.getParameter("city");
        final String salary = request.getParameter("salary");
        final String employment = request.getParameter("employmentType");
        final int experience = receiveExperience(request);
        final String description = request.getParameter("description");

        Optional<String> exception = validate(title, employerName, cityName, salary, employment);
        if (exception.isPresent()) {
            request.addAttributeToJsp(ERROR_FILLING_OUT_THE_FORM_PARAM, exception.get());
            return requestFactory.createForwardResponse(VACANCY_CREATION_PAGE);
        }

        final Employer employer = employerService.add(new Employer(employerName, ""));
        final City city = cityService.add(new City(cityName));
        vacancyService.add(new Vacancy(title, new BigDecimal(salary), employer, city, Employment.of(employment),
                experience, new Date(System.currentTimeMillis()), description));
        return requestFactory.createRedirectResponse("/controller");
    }

    Optional<String> validate(String title, String employer, String city, String salary, String employment) {
        if (title == null || title.isEmpty()) {
            return Optional.of(CITY_NAME_IS_EMPTY);
        } else if (employer == null || employer.isEmpty()) {
            return Optional.of(STREET_NAME_IS_EMPTY);
        } else if (city == null || city.isEmpty()) {
            return Optional.of(INCORRECT_HOSE_NUMBER);
        } else if (salary == null || salary.isEmpty()) {
            return Optional.of(INCORRECT_HOSE_NUMBER);
        } else if (employment == null || employment.equals("null")) {
            return Optional.of(INCORRECT_HOSE_NUMBER);
        } else {
            return Optional.empty();
        }
    }

    private int receiveExperience(CommandRequest request) {
        final String experience = request.getParameter("experience");
        if (!experience.isEmpty()) {
            return Integer.parseInt(experience);
        } else {
            return 0;
        }
    }
}
