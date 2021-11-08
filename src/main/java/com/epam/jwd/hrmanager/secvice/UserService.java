package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.dao.UserDao;
import com.epam.jwd.hrmanager.model.User;

import java.util.List;

public class UserService implements EntityService<User>{

    private final UserDao dao;

    UserService(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public List<User> findAll() {
        return dao.read();
    }
}
