package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.JobRequest;
import com.epam.jwd.hrmanager.model.Vacancy;

import java.util.List;

public interface JobRequestService extends EntityService<JobRequest> {

    List<JobRequest> findByAccount(Account account);

    List<JobRequest> findByVacancy(Vacancy vacancy);

}
