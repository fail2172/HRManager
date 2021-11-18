package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;
import com.epam.jwd.hrmanager.model.Employer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MethodEmployerDao extends CommonDao<Employer> implements EntityDao<Employer> {

    private static final Logger LOGGER = LogManager.getLogger(MethodEmployerDao.class);
    private static final String EMPLOYER_TABLE_NAME = "employer";
    private static final String ID_FIELD_NAME = "id";
    private static final String EMPLOYER_NAME_FIELD = "e_name";
    private static final String DESCRIPTION_FIELD_NAME = "description";
    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, EMPLOYER_NAME_FIELD,DESCRIPTION_FIELD_NAME);

    private MethodEmployerDao(ConnectionPool connectionPool){
        super(LOGGER, connectionPool);
    }

    static MethodEmployerDao getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    protected String getTableName() {
        return EMPLOYER_TABLE_NAME;
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
    protected Employer extractResultSet(ResultSet resultSet) throws SQLException {
        return new Employer(
                resultSet.getLong(ID_FIELD_NAME),
                resultSet.getString(EMPLOYER_NAME_FIELD),
                resultSet.getString(DESCRIPTION_FIELD_NAME)
        );
    }

    private static class Holder {
        private static final MethodEmployerDao INSTANCE = new MethodEmployerDao(ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.TRANSACTION_CONNECTION_POOL));
    }
}
