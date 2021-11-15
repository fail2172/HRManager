package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Interview;

import java.util.Optional;

public interface InterviewDao extends EntityDao<Interview> {

    Optional<Long> receiveAddressId(Interview interview);

    Optional<Long> receiveUserId(Interview interview);

    Optional<Long> receiveVacancyId(Interview interview);

}
