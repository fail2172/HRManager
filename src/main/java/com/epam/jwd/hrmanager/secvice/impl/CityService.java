package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.exeption.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.City;
import com.epam.jwd.hrmanager.secvice.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class CityService implements EntityService<City> {

    private static final TransactionManager transactionManager = TransactionManager.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(AddressService.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static CityService instance;

    private final EntityDao<City> cityDao;

    private CityService(EntityDao<City> cityDao) {
        this.cityDao = cityDao;
    }

    static CityService getInstance(EntityDao<City> cityDao) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new CityService(cityDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }


    @Override
    public City get(Long id) {
        try {
            transactionManager.initTransaction();
            return cityDao.read(id).orElse(null);
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public List<City> findAll() {
        try {
            transactionManager.initTransaction();
            return cityDao.read();
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public City add(City city) {
        try {
            transactionManager.initTransaction();
            return cityDao.create(city);
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding city to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public City update(City city) {
        try {
            transactionManager.initTransaction();
            return cityDao.update(city);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update city information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such city in the database", e);
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        try {
            transactionManager.initTransaction();
            return cityDao.delete(id);
        } finally {
            transactionManager.commitTransaction();
        }
    }
}
