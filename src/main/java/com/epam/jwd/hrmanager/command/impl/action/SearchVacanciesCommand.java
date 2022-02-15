package com.epam.jwd.hrmanager.command.impl.action;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.City;
import com.epam.jwd.hrmanager.model.Employer;
import com.epam.jwd.hrmanager.model.Vacancy;
import com.epam.jwd.hrmanager.secvice.CityService;
import com.epam.jwd.hrmanager.secvice.EmployerService;
import com.epam.jwd.hrmanager.secvice.VacancyService;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class SearchVacanciesCommand implements Command {

    private static final String SESSION_VARIABLE_PROPERTY = "sessionVariable";

    private static final String COMMAND_MAIN_PAGE_PROPERTY = "command/main_page";

    private static final String SEARCH_ATTRIBUTE_NAME = "search";

    private static final ReentrantLock lock = new ReentrantLock();
    private static SearchVacanciesCommand instance;

    private final RequestFactory requestFactory;
    private final VacancyService vacancyService;
    private final CityService cityService;
    private final EmployerService employerService;
    private final PropertyContext propertyContext;

    private SearchVacanciesCommand(RequestFactory requestFactory, VacancyService vacancyService,
                                   CityService cityService, EmployerService employerService,
                                   PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.vacancyService = vacancyService;
        this.cityService = cityService;
        this.employerService = employerService;
        this.propertyContext = propertyContext;
    }

    public static SearchVacanciesCommand getInstance(RequestFactory requestFactory, VacancyService vacancyService,
                                                     CityService cityService, EmployerService employerService,
                                                     PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new SearchVacanciesCommand(requestFactory, vacancyService,
                            cityService, employerService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String searchWord = request.getParameter(SEARCH_ATTRIBUTE_NAME);
        if (searchWord != null) {
            List<Vacancy> vacancies = findVacancies(searchWord);
            request.addToSession(propertyContext.get(SESSION_VARIABLE_PROPERTY), vacancies);
        }
        return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_MAIN_PAGE_PROPERTY));
    }

    private List<Vacancy> findVacancies(String searchWord) {
        final List<Vacancy> vacancies = new ArrayList<>();
        vacancies.addAll(searchByTitle(searchWord));
        vacancies.addAll(searchByCity(searchWord));
        vacancies.addAll(searchByEmployer(searchWord));
        return vacancies;
    }

    private List<Vacancy> searchByTitle(String title) {
        return vacancyService.findAll().stream()
                .filter(vacancy -> vacancy.getTitle().equals(title))
                .collect(Collectors.toList());
    }

    private List<Vacancy> searchByCity(String cityName) {
        final Optional<City> city = cityService.receiveByName(cityName);
        return city.map(value -> vacancyService.findAll().stream()
                .filter(vacancy -> vacancy.getCity().equals(value))
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    private List<Vacancy> searchByEmployer(String employerName) {
        final Optional<Employer> employer = employerService.receiveByName(employerName);
        return employer.map(value -> vacancyService.findAll().stream()
                .filter(vacancy -> vacancy.getEmployer().equals(value))
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }
}
