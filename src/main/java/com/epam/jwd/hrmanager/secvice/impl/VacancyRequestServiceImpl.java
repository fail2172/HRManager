package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.VacancyRequestDao;
import com.epam.jwd.hrmanager.exception.EntityUpdateException;
import com.epam.jwd.hrmanager.exception.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.AccountService;
import com.epam.jwd.hrmanager.secvice.VacancyRequestService;
import com.epam.jwd.hrmanager.secvice.VacancyService;
import com.epam.jwd.hrmanager.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class VacancyRequestServiceImpl implements VacancyRequestService {

    private static final Logger LOGGER = LogManager.getLogger(VacancyRequestServiceImpl.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static VacancyRequestServiceImpl instance;

    private final VacancyRequestDao vacancyRequestDao;
    private final VacancyService vacancyService;
    private final AccountService accountService;

    public VacancyRequestServiceImpl(VacancyRequestDao vacancyRequestDao, VacancyService vacancyService,
                                     AccountService accountService) {
        this.vacancyRequestDao = vacancyRequestDao;
        this.vacancyService = vacancyService;
        this.accountService = accountService;
    }


    static VacancyRequestServiceImpl getInstance(VacancyRequestDao vacancyRequestDao, VacancyService vacancyService,
                                                 AccountService accountService) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new VacancyRequestServiceImpl(vacancyRequestDao, vacancyService, accountService);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Transactional
    public VacancyRequest get(Long id) {
        final VacancyRequest vacancyRequest = vacancyRequestDao.read(id).orElse(null);
        final Long vacancyId = vacancyRequestDao.receiveVacancyId(vacancyRequest);
        final Long accountId = vacancyRequestDao.receiveAccountId(vacancyRequest);
        final Vacancy vacancy = vacancyService.get(vacancyId);
        final Account account = accountService.get(accountId);
        return Objects.requireNonNull(vacancyRequest).withVacancy(vacancy).withAccount(account);
    }

    @Override
    @Transactional
    public List<VacancyRequest> findAll() {
        return vacancyRequestDao.read().stream()
                .map(vacancyRequest -> get(vacancyRequest.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VacancyRequest add(VacancyRequest vacancyRequest) {
        try {
            final VacancyRequest addedVacancyRequest = vacancyRequestDao.create(vacancyRequest);
            return get(addedVacancyRequest.getId());
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding vacancy request to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
        }
        return null;
    }

    @Override
    @Transactional
    public VacancyRequest update(VacancyRequest vacancyRequest) {
        try {
            final VacancyRequest updatedVacancyRequest = vacancyRequestDao.update(vacancyRequest
                    .withVacancy(vacancyRequest.getVacancy())
                    .withAccount(vacancyRequest.getAccount())
                    .withStatus(vacancyRequest.getStatus()));
            return get(updatedVacancyRequest.getId());
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
        return vacancyRequestDao.delete(id);
    }
}
