package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.JobRequest;

public interface JobRequestDao extends EntityDao<JobRequest> {

    Long receiveVacancyId(JobRequest jobRequest);

    Long receiveAccountId(JobRequest jobRequest);

}
