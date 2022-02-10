package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.exception.EntityUpdateException;
import com.epam.jwd.hrmanager.exception.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.City;
import com.epam.jwd.hrmanager.secvice.CityService;
import com.epam.jwd.hrmanager.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = LogManager.getLogger(AddressServiceImpl.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static CityServiceImpl instance;

    private final EntityDao<City> cityDao;

    private CityServiceImpl(EntityDao<City> cityDao) {
        this.cityDao = cityDao;
    }

    static CityServiceImpl getInstance(EntityDao<City> cityDao) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new CityServiceImpl(cityDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }


    @Override
    @Transactional
    public City get(Long id) {
        return cityDao.read(id).orElse(null);
    }

    @Override
    @Transactional
    public List<City> findAll() {
        return cityDao.read();
    }

    @Override
    @Transactional
    public City add(City city) {
        try {
            return cityDao.create(city);
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding city to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted", e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    @Override
    @Transactional
    public City update(City city) {
        try {
            return cityDao.update(city);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update city information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such city in the database", e);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return cityDao.delete(id);
    }
}
