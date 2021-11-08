package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.User;

public interface UserDao extends EntityDao<User> {

    static UserDao getInstance(){
        return MethodUserDao.getInstance();
    }

}
