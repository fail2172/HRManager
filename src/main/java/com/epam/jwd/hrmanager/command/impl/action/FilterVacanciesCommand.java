package com.epam.jwd.hrmanager.command.impl.action;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.Employment;
import com.epam.jwd.hrmanager.model.Vacancy;
import com.epam.jwd.hrmanager.secvice.VacancyService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class FilterVacanciesCommand implements Command {

    private static final String COMMAND_MAIN_PAGE_PROPERTY = "command/main_page";

    private static final String VACANCIES_ATTRIBUTE_PARAM = "vacancies";
    private static final String CITY_VACANCIES_PARAM = "cityVacancies";
    private static final String EMPLOYMENT_TYPE_REQUEST_PARAM = "employmentType";
    private static final String EXPERIENCE_REQUEST_PARAM = "experience";
    private static final String INCOME_LEVEL_REQUEST_PARAM = "incomeLevel";
    private static final String SPACE = " ";

    private static final ReentrantLock lock = new ReentrantLock();
    private static final int ZERO = 0;
    private static FilterVacanciesCommand instance;

    private final RequestFactory requestFactory;
    private final VacancyService vacancyService;
    private final PropertyContext propertyContext;

    private FilterVacanciesCommand(RequestFactory requestFactory, VacancyService vacancyService,
                                   PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.vacancyService = vacancyService;
        this.propertyContext = propertyContext;
    }

    public static FilterVacanciesCommand getInstance(RequestFactory requestFactory, VacancyService vacancyService,
                                                     PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new FilterVacanciesCommand(requestFactory, vacancyService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Vacancy> vacancies = findVacancies(request);
        request.addToSession(VACANCIES_ATTRIBUTE_PARAM, vacancies);
        return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_MAIN_PAGE_PROPERTY));
    }

    private List<Vacancy> findVacancies(CommandRequest request) {
        List<Vacancy> vacancies = vacancyService.findAll();
        vacancies = filterByCities(vacancies, request);
        vacancies = filterByEmployment(vacancies, request);
        vacancies = filterByExperience(vacancies, request);
        vacancies = filterByIncomeLevel(vacancies, request);
        return vacancies;
    }

    private List<Vacancy> filterByCities(List<Vacancy> vacancies, CommandRequest request) {
        final String cityCriterion = request.getParameter(CITY_VACANCIES_PARAM);
        if (cityCriterion.isEmpty()) {
            return vacancies;
        }
        return vacancies.stream()
                .filter(vacancy -> vacancy.getCity().getName().equals(cityCriterion))
                .collect(Collectors.toList());
    }

    private List<Vacancy> filterByEmployment(List<Vacancy> vacancies, CommandRequest request) {
        final String employmentType = request.getParameter(EMPLOYMENT_TYPE_REQUEST_PARAM);
        if (employmentType.isEmpty()) {
            return vacancies;
        }
        return vacancies.stream()
                .filter(vacancy -> vacancy.getEmployment().equals(Employment.of(employmentType)))
                .collect(Collectors.toList());
    }

    private List<Vacancy> filterByExperience(List<Vacancy> vacancies, CommandRequest request) {
        final String experience = request.getParameter(EXPERIENCE_REQUEST_PARAM);
        if (experience.isEmpty()) {
            return vacancies;
        }
        List<Integer> limits = Arrays.stream(experience.split(SPACE))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return vacancies.stream()
                .filter(
                        vacancy -> limits.get(0) < vacancy.getExperience() && vacancy.getExperience() < limits.get(1)
                )
                .collect(Collectors.toList());
    }

    private List<Vacancy> filterByIncomeLevel(List<Vacancy> vacancies, CommandRequest request) {
        final String incomeLevel = request.getParameter(INCOME_LEVEL_REQUEST_PARAM);
        if (incomeLevel.isEmpty()) {
            return vacancies;
        }
        return vacancies.stream()
                .filter(vacancy -> vacancy.getSalary().compareTo(new BigDecimal(incomeLevel)) >= ZERO)
                .collect(Collectors.toList());
    }
}
