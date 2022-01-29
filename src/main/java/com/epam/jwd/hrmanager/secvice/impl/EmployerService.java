package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.exeption.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.Employer;
import com.epam.jwd.hrmanager.secvice.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class EmployerService implements EntityService<Employer> {

    private static final TransactionManager transactionManager = TransactionManager.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(AddressService.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static EmployerService instance;

    private final EntityDao<Employer> employerDao;

    private EmployerService(EntityDao<Employer> employerDao) {
        this.employerDao = employerDao;
    }

    static EmployerService getInstance(EntityDao<Employer> employerDao) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new EmployerService(employerDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }


    @Override
    public Employer get(Long id) {
        try {
            transactionManager.initTransaction();
            return employerDao.read(id).orElse(null);
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public List<Employer> findAll() {
        try {
            transactionManager.initTransaction();
            return employerDao.read();
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public Employer add(Employer employer) {
        try {
            transactionManager.initTransaction();
            return employerDao.create(employer);
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding employer to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public Employer update(Employer employer) {
        try {
            transactionManager.initTransaction();
            return employerDao.update(employer);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update employer information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such employer in the database", e);
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        try {
            transactionManager.initTransaction();
            return employerDao.delete(id);
        } finally {
            transactionManager.commitTransaction();
        }
    }
}
