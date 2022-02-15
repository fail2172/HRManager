package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.dao.impl.DaoFactoryImpl;
import com.epam.jwd.hrmanager.model.Entity;

public interface DaoFactory {

    <T extends Entity> EntityDao<T> daoFor(Class<T> modelDao);

    static DaoFactory getInstance() {
        return DaoFactoryImpl.getInstance();
    }

}
