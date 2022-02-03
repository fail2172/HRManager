package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.exeption.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class UserService implements EntityService<User> {

    private static final TransactionManager transactionManager = TransactionManager.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private static final ReentrantLock lock = new ReentrantLock();


    private static UserService instance;

    private final EntityDao<User> userDao;

    UserService(EntityDao<User> userDao) {
        this.userDao = userDao;
    }

    static UserService getInstance(EntityDao<User> userDao) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new UserService(userDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public User get(Long id) {
        try {
            transactionManager.initTransaction();
            return userDao.read(id).orElse(null);
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public List<User> findAll() {
        try {
            transactionManager.initTransaction();
            return userDao.read();
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public User add(User user) {
        try {
            transactionManager.initTransaction();
            return userDao.create(user);
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding address to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public User update(User user) {
        try {
            transactionManager.initTransaction();
            User updatedUser = userDao.update(user
                    .withFirstName(user.getFirstName())
                    .withSecondName(user.getSecondName()));
            return get(updatedUser.getId());
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update user information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such user in the database", e);
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        try {
            transactionManager.initTransaction();
            return userDao.delete(id);
        } finally {
            transactionManager.commitTransaction();
        }
    }


}
