package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class UserService implements EntityService<User> {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    private static UserService instance;
    private static final ReentrantLock lock = new ReentrantLock();

    private final EntityDao<User> userDao;

    UserService(EntityDao<User> userDao) {
        this.userDao = userDao;
    }

    static UserService getInstance(EntityDao<User> userDao){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new UserService(userDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public User get(Long id) {
        return userDao.read(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userDao.read();
    }

    @Override
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
    public User update(User user) {
        return null;
    }
}
