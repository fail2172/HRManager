package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.DaoFactory;
import com.epam.jwd.hrmanager.dao.UserDao;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.EntityService;

import java.util.List;
import java.util.stream.Collectors;

public class UserService implements EntityService<User> {

    private final UserDao userDao;

    UserService(UserDao userDao) {
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
        return userDao.read().stream()
                .map(user -> get(user.getId()))
                .collect(Collectors.toList());
    }

    private static class Holder {
        private static final UserService INSTANCE = new UserService(
                (UserDao) DaoFactory.getInstance().daoFor(User.class)
        );
    }
}
