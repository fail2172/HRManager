package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Vacancy;

import java.util.Optional;

public interface VacancyDao extends EntityDao<Vacancy> {

    Long receiveEmployerId(Vacancy vacancy);

    Long receiveCityId(Vacancy vacancy);

}
