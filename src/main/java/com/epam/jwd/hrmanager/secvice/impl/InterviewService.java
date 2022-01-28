package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.InterviewDao;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class InterviewService implements EntityService<Interview> {

    private static final TransactionManager transactionManager = TransactionManager.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(AddressService.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static InterviewService instance;

    private final InterviewDao interviewDao;
    private final EntityService<Address> addressService;
    private final EntityService<User> userService;
    private final EntityService<Vacancy> vacancyService;

    private InterviewService(InterviewDao interviewDao, EntityService<Address> addressService,
                             EntityService<User> userService, EntityService<Vacancy> vacancyService) {
        this.interviewDao = interviewDao;
        this.addressService = addressService;
        this.userService = userService;
        this.vacancyService = vacancyService;
    }

    static InterviewService getInstance(InterviewDao interviewDao, EntityService<Address> addressService,
                                        EntityService<User> userService, EntityService<Vacancy> vacancyService) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new InterviewService(interviewDao, addressService, userService, vacancyService);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public Interview get(Long id) {
        transactionManager.initTransaction();
        Interview interview = interviewDao.read(id).orElse(null);
        final Long addressId = interviewDao.receiveAddressId(interview).orElse(null);
        final Long userId = interviewDao.receiveUserId(interview).orElse(null);
        final Long vacancyId = interviewDao.receiveVacancyId(interview).orElse(null);
        Address address = addressService.get(addressId);
        User user = userService.get(userId);
        Vacancy vacancy = vacancyService.get(vacancyId);
        transactionManager.commitTransaction();
        return Objects.requireNonNull(interview).withAddress(address).withUser(user).withVacancy(vacancy);
    }

    @Override
    public List<Interview> findAll() {
        return interviewDao.read().stream()
                .map(interview -> get(interview.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Interview add(Interview interview) {
        try {
            transactionManager.initTransaction();
            final Interview addedInterview = interviewDao.create(interview
                    .withAddress(addressService.add(interview.getAddress()))
                    .withUser(userService.add(interview.getUser()))
                    .withVacancy(vacancyService.add(interview.getVacancy())));
            return get(addedInterview.getId());
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding address to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public Interview update(Interview interview) {
        return null;
    }
}
