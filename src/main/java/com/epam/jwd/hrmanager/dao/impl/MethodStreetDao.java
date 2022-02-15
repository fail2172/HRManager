package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.dao.StreetDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.model.Street;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MethodStreetDao extends CommonDao<Street> implements StreetDao {

    private static final String STREET_TABLE_NAME = "street";
    private static final String ID_FIELD_NAME = "id";
    private static final String STREET_NAME_FIELD = "s_name";

    private static final Logger LOGGER = LogManager.getLogger(MethodStreetDao.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Integer ZERO = 0;
    private static MethodStreetDao instance;
    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, STREET_NAME_FIELD);

    private MethodStreetDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    static MethodStreetDao getInstance(ConnectionPool connectionPool){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new MethodStreetDao(connectionPool);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    protected String getTableName() {
        return STREET_TABLE_NAME;
    }

    @Override
    protected String getIdFieldName() {
        return ID_FIELD_NAME;
    }

    @Override
    protected String getUniqueFieldName() {
        return STREET_NAME_FIELD;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Street street) throws SQLException {
        statement.setLong(1, ZERO);
        statement.setString(2, street.getName());
    }

    @Override
    protected void updateEntity(PreparedStatement statement, Street street) throws SQLException {
        statement.setLong(1, street.getId());
        statement.setString(2, street.getName());
        statement.setLong(3, street.getId());
    }

    @Override
    protected void fillUniqueField(PreparedStatement statement, Street entity) throws SQLException {
        statement.setString(1, entity.getName());
    }

    @Override
    protected Street extractResultSet(ResultSet resultSet) throws SQLException {
        return new Street(
                resultSet.getLong(ID_FIELD_NAME),
                resultSet.getString(STREET_NAME_FIELD)
        );
    }
}
