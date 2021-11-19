package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.InterviewDao;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.EntityService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class InterviewService implements EntityService<Interview> {

    private static InterviewService instance;
    private static final ReentrantLock lock = new ReentrantLock();

    private final InterviewDao interviewDao;
    private final EntityService<Address> addressService;
    private final EntityService<User> userService;
    private final EntityService<Vacancy> vacancyService;
    private static final TransactionManager transactionManager = TransactionManager.getInstance();

    private InterviewService(InterviewDao interviewDao, EntityService<Address> addressService,
                             EntityService<User> userService, EntityService<Vacancy> vacancyService){
        this.interviewDao = interviewDao;
        this.addressService = addressService;
        this.userService = userService;
        this.vacancyService = vacancyService;
    }

    static InterviewService getInstance(InterviewDao interviewDao, EntityService<Address> addressService,
                                        EntityService<User> userService, EntityService<Vacancy> vacancyService){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
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
}
