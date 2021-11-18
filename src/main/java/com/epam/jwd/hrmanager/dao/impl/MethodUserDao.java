package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;
import com.epam.jwd.hrmanager.model.Role;
import com.epam.jwd.hrmanager.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MethodUserDao extends CommonDao<User> implements EntityDao<User> {

    private static final Logger LOGGER = LogManager.getLogger(MethodUserDao.class);
    private static final String USER_TABLE_NAME = "hr_user u join user_role r on role_id = r.id";
    private static final String ID_FIELD_NAME = "u.id";
    private static final String FIRST_NAME_FIELD_NAME = "u.first_name";
    private static final String SECOND_NAME_FIELD_NAME = "u.second_name";
    private static final String U_ROLE_FIELD_NAME = "r.r_name";
    private static final List<String> FIELDS = Arrays.asList(
            ID_FIELD_NAME, FIRST_NAME_FIELD_NAME, SECOND_NAME_FIELD_NAME, U_ROLE_FIELD_NAME
    );

    private MethodUserDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    public static MethodUserDao getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    protected String getTableName() {
        return USER_TABLE_NAME;
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
    protected User extractResultSet(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong(ID_FIELD_NAME),
                Role.of(resultSet.getString(U_ROLE_FIELD_NAME)),
                resultSet.getString(FIRST_NAME_FIELD_NAME),
                resultSet.getString(SECOND_NAME_FIELD_NAME)
        );
    }

    private static class Holder {
        public static final MethodUserDao INSTANCE = new MethodUserDao(ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.TRANSACTION_CONNECTION_POOL));
    }

}
