package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.dao.VacancyDao;
import com.epam.jwd.hrmanager.model.City;
import com.epam.jwd.hrmanager.model.Employer;
import com.epam.jwd.hrmanager.model.Vacancy;
import com.epam.jwd.hrmanager.secvice.EntityService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class VacancyService implements EntityService<Vacancy> {

    private static VacancyService instance;
    private static final ReentrantLock lock = new ReentrantLock();

    private final VacancyDao vacancyDao;
    private final EntityDao<Employer> employerDao;
    private final EntityDao<City> cityDao;

    private VacancyService(VacancyDao vacancyDao, EntityDao<Employer> employerDao, EntityDao<City> cityDao) {
        this.vacancyDao = vacancyDao;
        this.employerDao = employerDao;
        this.cityDao = cityDao;
    }

    static VacancyService getInstance(VacancyDao vacancyDao, EntityDao<Employer> employerDao, EntityDao<City> cityDao){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new VacancyService(vacancyDao, employerDao, cityDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public Vacancy get(Long id) {
        Vacancy vacancy = vacancyDao.read(id).orElse(null);
        final Long employerId = vacancyDao.receiveEmployerId(vacancy).orElse(null);
        final Long cityId = vacancyDao.receiveCityId(vacancy).orElse(null);
        Employer employer = employerDao.read(employerId).orElse(null);
        City city = cityDao.read(cityId).orElse(null);
        return Objects.requireNonNull(vacancy).withEmployer(employer).withCity(city);
    }

    @Override
    public List<Vacancy> findAll() {
        return vacancyDao.read().stream()
                .map(vacancy -> get(vacancy.getId()))
                .collect(Collectors.toList());
    }
}
