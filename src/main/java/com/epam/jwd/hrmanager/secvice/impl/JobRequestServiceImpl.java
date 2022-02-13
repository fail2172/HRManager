package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.JobRequestDao;
import com.epam.jwd.hrmanager.exception.EntityUpdateException;
import com.epam.jwd.hrmanager.exception.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.AccountService;
import com.epam.jwd.hrmanager.secvice.JobRequestService;
import com.epam.jwd.hrmanager.secvice.VacancyService;
import com.epam.jwd.hrmanager.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class JobRequestServiceImpl implements JobRequestService {

    private static final Logger LOGGER = LogManager.getLogger(JobRequestServiceImpl.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static JobRequestServiceImpl instance;

    private final JobRequestDao jobRequestDao;
    private final VacancyService vacancyService;
    private final AccountService accountService;

    public JobRequestServiceImpl(JobRequestDao jobRequestDao, VacancyService vacancyService,
                                 AccountService accountService) {
        this.jobRequestDao = jobRequestDao;
        this.vacancyService = vacancyService;
        this.accountService = accountService;
    }


    static JobRequestServiceImpl getInstance(JobRequestDao jobRequestDao, VacancyService vacancyService,
                                             AccountService accountService) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new JobRequestServiceImpl(jobRequestDao, vacancyService, accountService);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Transactional
    public JobRequest get(Long id) {
        final JobRequest jobRequest = jobRequestDao.read(id).orElse(null);
        final Long vacancyId = jobRequestDao.receiveVacancyId(jobRequest);
        final Long accountId = jobRequestDao.receiveAccountId(jobRequest);
        final Vacancy vacancy = vacancyService.get(vacancyId);
        final Account account = accountService.get(accountId);
        return Objects.requireNonNull(jobRequest).withVacancy(vacancy).withAccount(account);
    }

    @Override
    @Transactional
    public List<JobRequest> findAll() {
        return jobRequestDao.read().stream()
                .map(vacancyRequest -> get(vacancyRequest.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobRequest add(JobRequest jobRequest) {
        try {
            final JobRequest addedJobRequest = jobRequestDao.create(jobRequest);
            return get(addedJobRequest.getId());
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding vacancy request to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
        }
        return null;
    }

    @Override
    @Transactional
    public JobRequest update(JobRequest jobRequest) {
        try {
            final JobRequest updatedJobRequest = jobRequestDao.update(jobRequest
                    .withVacancy(jobRequest.getVacancy())
                    .withAccount(jobRequest.getAccount())
                    .withStatus(jobRequest.getStatus()));
            return get(updatedJobRequest.getId());
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update vacancy request information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such vacancy request in the database", e);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return jobRequestDao.delete(id);
    }

    @Override
    public List<JobRequest> findByAccount(Account account) {
        return jobRequestDao.receiveJobRequestsByAccount(account).stream()
                .map(jr -> get(jr.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobRequest> findByVacancy(Vacancy vacancy) {
        return jobRequestDao.receiveJobRequestsByVacancy(vacancy).stream()
                .map(jr -> get(jr.getId()))
                .collect(Collectors.toList());
    }
}
