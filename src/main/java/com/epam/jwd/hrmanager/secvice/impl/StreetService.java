package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.exeption.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.Street;
import com.epam.jwd.hrmanager.secvice.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class StreetService implements EntityService<Street> {

    private static final TransactionManager transactionManager = TransactionManager.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(AddressService.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static StreetService instance;

    private final EntityDao<Street> streetDao;

    private StreetService(EntityDao<Street> streetDao) {
        this.streetDao = streetDao;
    }

    static StreetService getInstance(EntityDao<Street> streetDao) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new StreetService(streetDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }


    @Override
    public Street get(Long id) {
        try {
            transactionManager.initTransaction();
            return streetDao.read(id).orElse(null);
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public List<Street> findAll() {
        try {
            transactionManager.initTransaction();
            return streetDao.read();
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public Street add(Street street) {
        try {
            transactionManager.initTransaction();
            return streetDao.create(street);
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding street to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public Street update(Street street) {
        try {
            transactionManager.initTransaction();
            return streetDao.update(street);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update street information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such street in the database", e);
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        try {
            transactionManager.initTransaction();
            return streetDao.delete(id);
        } finally {
            transactionManager.commitTransaction();
        }
    }
}
