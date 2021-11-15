package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.DaoFactory;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.EntityService;

import java.util.List;

public class UserService implements EntityService<User> {

    private final EntityDao<User> userDao;

    UserService(EntityDao<User> userDao) {
        this.userDao = userDao;
    }

    static UserService getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public User get(Long id) {
        return userDao.read(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userDao.read();
    }

    private static class Holder {
        private static final UserService INSTANCE = new UserService(
                DaoFactory.getInstance().daoFor(User.class)
        );
    }
}
