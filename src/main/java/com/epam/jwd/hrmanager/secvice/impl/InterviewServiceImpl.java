package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.InterviewDao;
import com.epam.jwd.hrmanager.exception.EntityUpdateException;
import com.epam.jwd.hrmanager.exception.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.EntityService;
import com.epam.jwd.hrmanager.secvice.InterviewService;
import com.epam.jwd.hrmanager.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class InterviewServiceImpl implements InterviewService {

    private static final Logger LOGGER = LogManager.getLogger(AddressServiceImpl.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static InterviewServiceImpl instance;

    private final InterviewDao interviewDao;
    private final EntityService<Address> addressService;
    private final EntityService<User> userService;
    private final EntityService<Vacancy> vacancyService;

    private InterviewServiceImpl(InterviewDao interviewDao, EntityService<Address> addressService,
                                 EntityService<User> userService, EntityService<Vacancy> vacancyService) {
        this.interviewDao = interviewDao;
        this.addressService = addressService;
        this.userService = userService;
        this.vacancyService = vacancyService;
    }

    static InterviewServiceImpl getInstance(InterviewDao interviewDao, EntityService<Address> addressService,
                                            EntityService<User> userService, EntityService<Vacancy> vacancyService) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new InterviewServiceImpl(interviewDao, addressService, userService, vacancyService);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Transactional
    public Interview get(Long id) {
        Interview interview = interviewDao.read(id).orElse(null);
        final Long addressId = interviewDao.receiveAddressId(interview);
        final Long userId = interviewDao.receiveUserId(interview);
        final Long vacancyId = interviewDao.receiveVacancyId(interview);
        Address address = addressService.get(addressId);
        User user = userService.get(userId);
        Vacancy vacancy = vacancyService.get(vacancyId);
        return Objects.requireNonNull(interview).withAddress(address).withUser(user).withVacancy(vacancy);
    }

    @Override
    @Transactional
    public List<Interview> findAll() {
            return interviewDao.read().stream()
                    .map(interview -> get(interview.getId()))
                    .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Interview add(Interview interview) {
        try {
            final Interview addedInterview = interviewDao.create(interview
                    .withAddress(interview.getAddress())
                    .withUser(interview.getUser())
                    .withVacancy(interview.getVacancy()));
            return get(addedInterview.getId());
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding address to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
        }
        return null;
    }

    @Override
    @Transactional
    public Interview update(Interview interview) {
        try {
            Interview updatedInterview = interviewDao.update(interview
                    .withInterviewStatus(interview.getStatus())
                    .withAddress(interview.getAddress())
                    .withUser(interview.getUser())
                    .withVacancy(interview.getVacancy())
                    .withData(interview.getDate()));
            return get(updatedInterview.getId());
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update interview information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such interview in the database", e);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return interviewDao.delete(id);
    }
}
