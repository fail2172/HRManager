package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.dao.impl.MethodUserDao;
import com.epam.jwd.hrmanager.model.User;

import java.util.Optional;

public interface UserDao extends EntityDao<User> {

    static UserDao getInstance(){
        return MethodUserDao.getInstance();
    }

    Optional<Long> receiveRoleId(User user);

}
