package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.JobRequest;

import java.util.List;

public interface JobRequestService extends EntityService<JobRequest> {

    List<JobRequest> findByAccount(Account account);

}
