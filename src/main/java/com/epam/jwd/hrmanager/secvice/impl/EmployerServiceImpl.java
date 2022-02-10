package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.exception.EntityUpdateException;
import com.epam.jwd.hrmanager.exception.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.Employer;
import com.epam.jwd.hrmanager.secvice.EmployerService;
import com.epam.jwd.hrmanager.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class EmployerServiceImpl implements EmployerService {

    private static final Logger LOGGER = LogManager.getLogger(AddressServiceImpl.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static EmployerServiceImpl instance;

    private final EntityDao<Employer> employerDao;

    private EmployerServiceImpl(EntityDao<Employer> employerDao) {
        this.employerDao = employerDao;
    }

    static EmployerServiceImpl getInstance(EntityDao<Employer> employerDao) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new EmployerServiceImpl(employerDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }


    @Override
    @Transactional
    public Employer get(Long id) {
        return employerDao.read(id).orElse(null);
    }

    @Override
    @Transactional
    public List<Employer> findAll() {
        return employerDao.read();
    }

    @Override
    @Transactional
    public Employer add(Employer employer) {
        try {
            return employerDao.create(employer);
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding employer to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted", e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    @Override
    @Transactional
    public Employer update(Employer employer) {
        try {
            return employerDao.update(employer);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update employer information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such employer in the database", e);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return employerDao.delete(id);
    }
}
