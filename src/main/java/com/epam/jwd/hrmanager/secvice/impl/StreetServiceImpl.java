package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.StreetDao;
import com.epam.jwd.hrmanager.exception.EntityUpdateException;
import com.epam.jwd.hrmanager.exception.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.Street;
import com.epam.jwd.hrmanager.secvice.StreetService;
import com.epam.jwd.hrmanager.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class StreetServiceImpl implements StreetService {

    private static final Logger LOGGER = LogManager.getLogger(AddressServiceImpl.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static StreetServiceImpl instance;

    private final StreetDao streetDao;

    private StreetServiceImpl(StreetDao streetDao) {
        this.streetDao = streetDao;
    }

    static StreetServiceImpl getInstance(StreetDao streetDao) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new StreetServiceImpl(streetDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }


    @Override
    @Transactional
    public Street get(Long id) {
        return streetDao.read(id).orElse(null);
    }

    @Override
    @Transactional
    public List<Street> findAll() {
        return streetDao.read();
    }

    @Override
    @Transactional
    public Street add(Street street) {
        try {
            return streetDao.create(street);
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding street to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted", e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    @Override
    @Transactional
    public Street update(Street street) {
        try {
            return streetDao.update(street);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update street information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such street in the database", e);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return streetDao.delete(id);
    }
}
