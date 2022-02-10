package com.epam.jwd.hrmanager.command.impl;

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

    private static final String VACANCIES_ATTRIBUTE_NAME = "vacancies";
    private static final String CITIES_ATTRIBUTE_NAME = "cities";
    private static final ReentrantLock lock = new ReentrantLock();
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

    static ShowMainPageCommand getInstance(RequestFactory requestFactory, VacancyService vacancyService,
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
        Optional<Object> parameterizedVacancies = request.retrieveFromSession(VACANCIES_ATTRIBUTE_NAME);
        if (parameterizedVacancies.isPresent()) {
            request.addAttributeToJsp(VACANCIES_ATTRIBUTE_NAME, parameterizedVacancies.get());
            request.removeFromSession(VACANCIES_ATTRIBUTE_NAME);
        } else {
            List<Vacancy> vacancies = vacancyService.findAll();
            request.addAttributeToJsp(VACANCIES_ATTRIBUTE_NAME, vacancies);
        }
        List<City> cities = cityService.findAll();
        request.addAttributeToJsp(CITIES_ATTRIBUTE_NAME, cities);
        return requestFactory.createForwardResponse(propertyContext.get(MAIN_PAGE));
    }
}
