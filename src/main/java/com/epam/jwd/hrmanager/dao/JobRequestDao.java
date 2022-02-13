package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.JobRequest;
import com.epam.jwd.hrmanager.model.Vacancy;

import java.util.List;

public interface JobRequestDao extends EntityDao<JobRequest> {

    Long receiveVacancyId(JobRequest jobRequest);

    Long receiveAccountId(JobRequest jobRequest);

    List<JobRequest> receiveJobRequestsByAccount(Account account);

    List<JobRequest> receiveJobRequestsByVacancy(Vacancy vacancy);

}
