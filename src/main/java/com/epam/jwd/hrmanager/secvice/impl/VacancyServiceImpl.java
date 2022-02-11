package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.dao.VacancyDao;
import com.epam.jwd.hrmanager.exception.EntityUpdateException;
import com.epam.jwd.hrmanager.exception.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.VacancyService;
import com.epam.jwd.hrmanager.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class VacancyServiceImpl implements VacancyService {

    private static final Logger LOGGER = LogManager.getLogger(AddressServiceImpl.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static VacancyServiceImpl instance;

    private final VacancyDao vacancyDao;
    private final EntityDao<Employer> employerDao;
    private final EntityDao<City> cityDao;

    private VacancyServiceImpl(VacancyDao vacancyDao, EntityDao<Employer> employerDao, EntityDao<City> cityDao) {
        this.vacancyDao = vacancyDao;
        this.employerDao = employerDao;
        this.cityDao = cityDao;
    }

    static VacancyServiceImpl getInstance(VacancyDao vacancyDao, EntityDao<Employer> employerDao, EntityDao<City> cityDao) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new VacancyServiceImpl(vacancyDao, employerDao, cityDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Transactional
    public Vacancy get(Long id) {
        Vacancy vacancy = vacancyDao.read(id).orElse(null);
        final Long employerId = vacancyDao.receiveEmployerId(vacancy);
        final Long cityId = vacancyDao.receiveCityId(vacancy);
        Employer employer = employerDao.read(employerId).orElse(null);
        City city = cityDao.read(cityId).orElse(null);
        return Objects.requireNonNull(vacancy).withEmployer(employer).withCity(city);
    }

    @Override
    @Transactional
    public List<Vacancy> findAll() {
        return vacancyDao.read().stream()
                .map(vacancy -> get(vacancy.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Vacancy add(Vacancy vacancy) {
        try {
            final Vacancy addedVacancy = vacancyDao.create(vacancy);
            return get(addedVacancy.getId());
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding address to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
        }
        return null;
    }

    @Override
    @Transactional
    public Vacancy update(Vacancy vacancy) {
        try {
            Vacancy updatedVacancy = vacancyDao.update(vacancy
                            .withTitle(vacancy.getTitle())
                            .withSalary(vacancy.getSalary())
                            .withEmployer(vacancy.getEmployer())
                            .withEmployment(vacancy.getEmployment())
                            .withCity(vacancy.getCity())
                            .withDescription(vacancy.getDescription().orElse(null))
                            .withExperience(vacancy.getExperience()))
                    .withCity(vacancy.getCity())
                    .withEmployment(vacancy.getEmployment())
                    .withDescription(vacancy.getDescription().orElse(null));
            return get(updatedVacancy.getId());
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update vacancy information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such vacancy in the database", e);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return vacancyDao.delete(id);
    }
}
