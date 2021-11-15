package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Vacancy;

import java.util.Optional;

public interface VacancyDao extends EntityDao<Vacancy>{

    Optional<Long> receiveEmployerId(Vacancy vacancy);

    Optional<Long> receiveCityId(Vacancy vacancy);

}
