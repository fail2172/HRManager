package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.UserDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;
import com.epam.jwd.hrmanager.model.Role;
import com.epam.jwd.hrmanager.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MethodUserDao extends CommonDao<User> implements UserDao {

    private static final Logger LOGGER = LogManager.getLogger(MethodUserDao.class);
    private static final String USER_TABLE_NAME = "hr_user";
    private static final String ID_FIELD_NAME = "id";
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String SECOND_NAME_FIELD_NAME = "second_name";
    public static final String PASSWORD_FIELD_NAME = "password";
    public static final String ROLE_ID_FIELD_NAME = "role_id";

    private MethodUserDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    public static MethodUserDao getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Optional<Long> receiveRoleId(User user) {
        return receiveForeignKey(user, ROLE_ID_FIELD_NAME);
    }

    @Override
    protected String getTableName() {
        return USER_TABLE_NAME;
    }

    @Override
    protected User extractResultSet(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong(ID_FIELD_NAME),
                Role.of(resultSet.getString("u_role")),
                resultSet.getString(FIRST_NAME_FIELD_NAME),
                resultSet.getString(SECOND_NAME_FIELD_NAME),
                resultSet.getString(PASSWORD_FIELD_NAME)
        );
    }

    private static class Holder {
        public static final MethodUserDao INSTANCE = new MethodUserDao(ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.TRANSACTION_CONNECTION_POOL));
    }

}
