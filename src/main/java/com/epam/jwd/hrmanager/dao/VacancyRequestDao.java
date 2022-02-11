package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.VacancyRequest;

public interface VacancyRequestDao extends EntityDao<VacancyRequest> {

    Long receiveVacancyId(VacancyRequest vacancyRequest);

    Long receiveAccountId(VacancyRequest vacancyRequest);

}
