package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;
import com.epam.jwd.hrmanager.model.City;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MethodCityDao extends CommonDao<City> implements EntityDao<City> {

    private static final Logger LOGGER = LogManager.getLogger(MethodCityDao.class);
    private static final String CITY_TABLE_NAME = "city";
    private static final String ID_FIELD_NAME = "id";
    private static final String CITY_NAME_FIELD = "c_name";
    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, CITY_NAME_FIELD);

    private MethodCityDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    static MethodCityDao getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    protected String getTableName() {
        return CITY_TABLE_NAME;
    }

    @Override
    protected String getIdFieldName() {
        return ID_FIELD_NAME;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected City extractResultSet(ResultSet resultSet) throws SQLException {
        return new City(
                resultSet.getLong(ID_FIELD_NAME),
                resultSet.getString(CITY_NAME_FIELD)
        );
    }

    private static class Holder {
        private static final MethodCityDao INSTANCE = new MethodCityDao(ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.SIMPLE_CONNECTION_POOL));
    }
}
