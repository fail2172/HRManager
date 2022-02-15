package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.UserDao;
import com.epam.jwd.hrmanager.exception.EntityUpdateException;
import com.epam.jwd.hrmanager.exception.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.UserService;
import com.epam.jwd.hrmanager.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static UserServiceImpl instance;

    private final UserDao userDao;

    UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    static UserServiceImpl getInstance(UserDao userDao) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new UserServiceImpl(userDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Transactional
    public User get(Long id) {
        return userDao.read(id).orElse(null);
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return userDao.read();
    }

    @Override
    @Transactional
    public User add(User user) {
        try {
            return userDao.create(user);
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding address to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
        }
        return null;
    }

    @Override
    @Transactional
    public User update(User user) {
        try {
            User updatedUser = userDao.update(user);
            return get(updatedUser.getId());
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update user information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such user in the database", e);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return userDao.delete(id);
    }


}
