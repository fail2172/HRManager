package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.DaoFactory;
import com.epam.jwd.hrmanager.dao.InterviewDao;
import com.epam.jwd.hrmanager.model.Address;
import com.epam.jwd.hrmanager.model.Interview;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.model.Vacancy;
import com.epam.jwd.hrmanager.secvice.EntityService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InterviewService implements EntityService<Interview> {

    private final InterviewDao interviewDao;
    private final EntityService<Address> addressService;
    private final EntityService<User> userService;
    private final EntityService<Vacancy> vacancyService;

    private InterviewService(InterviewDao interviewDao, EntityService<Address> addressService,
                             EntityService<User> userService, EntityService<Vacancy> vacancyService){
        this.interviewDao = interviewDao;
        this.addressService = addressService;
        this.userService = userService;
        this.vacancyService = vacancyService;
    }

    static InterviewService getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    public Interview get(Long id) {
        Interview interview = interviewDao.read(id).orElse(null);
        final Long addressId = interviewDao.receiveAddressId(interview).orElse(null);
        final Long userId = interviewDao.receiveUserId(interview).orElse(null);
        final Long vacancyId = interviewDao.receiveVacancyId(interview).orElse(null);
        Address address = addressService.get(addressId);
        User user = userService.get(userId);
        Vacancy vacancy = vacancyService.get(vacancyId);
        return Objects.requireNonNull(interview).withAddress(address).withUser(user).withVacancy(vacancy);
    }

    @Override
    public List<Interview> findAll() {
        return interviewDao.read().stream()
                .map(interview -> get(interview.getId()))
                .collect(Collectors.toList());
    }

    private static class Holder {
        private static final InterviewService INSTANCE = new InterviewService(
                (InterviewDao) DaoFactory.getInstance().daoFor(Interview.class),
                AddressService.getInstance(),
                UserService.getInstance(),
                VacancyService.getInstance()
        );
    }
}
