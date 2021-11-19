package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;
import com.epam.jwd.hrmanager.model.Street;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MethodStreetDao extends CommonDao<Street> implements EntityDao<Street> {

    private static final Logger LOGGER = LogManager.getLogger(MethodStreetDao.class);
    private static final String STREET_TABLE_NAME = "street";
    private static final String ID_FIELD_NAME = "id";
    private static final String STREET_NAME_FIELD = "s_name";
    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, STREET_NAME_FIELD);

    private MethodStreetDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    static MethodStreetDao getInstance() {
        return Holder.INSTANCE;
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
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected Street extractResultSet(ResultSet resultSet) throws SQLException {
        return new Street(
                resultSet.getLong(ID_FIELD_NAME),
                resultSet.getString(STREET_NAME_FIELD)
        );
    }

    private static class Holder {
        private static final MethodStreetDao INSTANCE = new MethodStreetDao(ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.TRANSACTION_CONNECTION_POOL));
    }
}
