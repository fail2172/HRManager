package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Interview;

import java.util.Optional;

public interface InterviewDao extends EntityDao<Interview> {

    Long receiveAddressId(Interview interview);

    Long receiveUserId(Interview interview);

    Long receiveVacancyId(Interview interview);

}
