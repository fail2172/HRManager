package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.EntityService;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class UserService implements EntityService<User> {

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
}
