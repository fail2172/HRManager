package com.epam.jwd.hrmanager.command.impl.page;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.City;
import com.epam.jwd.hrmanager.model.Vacancy;
import com.epam.jwd.hrmanager.secvice.CityService;
import com.epam.jwd.hrmanager.secvice.VacancyService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ShowMainPageCommand implements Command {

    private static final String MAIN_PAGE = "page.main";

    private static final String VACANCIES_ATTRIBUTE = "vacancies";
    private static final String CITIES_ATTRIBUTE = "cities";

    private static final ReentrantLock lock = new ReentrantLock();
    private static final String SESSION_VARIABLE_PROPERTY = "session.variable";
    private static ShowMainPageCommand instance;

    private final RequestFactory requestFactory;
    private final VacancyService vacancyService;
    private final CityService cityService;
    private final PropertyContext propertyContext;

    private ShowMainPageCommand(RequestFactory requestFactory, VacancyService vacancyService,
                                CityService cityService, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.vacancyService = vacancyService;
        this.cityService = cityService;
        this.propertyContext = propertyContext;
    }

    public static ShowMainPageCommand getInstance(RequestFactory requestFactory, VacancyService vacancyService,
                                                  CityService cityService, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ShowMainPageCommand(requestFactory, vacancyService, cityService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Object> parameterizedVacancies = request.retrieveFromSession(propertyContext.get(SESSION_VARIABLE_PROPERTY));
        if (parameterizedVacancies.isPresent()) {
            request.addAttributeToJsp(VACANCIES_ATTRIBUTE, parameterizedVacancies.get());
            request.removeFromSession(propertyContext.get(SESSION_VARIABLE_PROPERTY));
        } else {
            List<Vacancy> vacancies = vacancyService.findAll();
            request.addAttributeToJsp(VACANCIES_ATTRIBUTE, vacancies);
        }
        List<City> cities = cityService.findAll();
        request.addAttributeToJsp(CITIES_ATTRIBUTE, cities);
        return requestFactory.createForwardResponse(propertyContext.get(MAIN_PAGE));
    }
}
