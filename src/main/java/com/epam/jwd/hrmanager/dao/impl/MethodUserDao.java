package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.model.Role;
import com.epam.jwd.hrmanager.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MethodUserDao extends CommonDao<User> implements EntityDao<User> {

    private static MethodUserDao instance;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Logger LOGGER = LogManager.getLogger(MethodUserDao.class);
    private static final String USER_TABLE_NAME = "hr_user";
    private static final String ID_FIELD_NAME = "id";
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String SECOND_NAME_FIELD_NAME = "second_name";
    private static final String U_ROLE_FIELD_NAME = "r_name";
    private static final String HASH = "u_hash";
    private static final Integer ZERO = 0;
    private static final List<String> FIELDS = Arrays.asList(
            ID_FIELD_NAME, FIRST_NAME_FIELD_NAME, SECOND_NAME_FIELD_NAME, HASH
    );

    private MethodUserDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    static MethodUserDao getInstance(ConnectionPool connectionPool) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new MethodUserDao(connectionPool);
                }
            }
            lock.unlock();
        }
        return instance;
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
    protected String getUniqueFieldName() {
        return HASH;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    /**
     * При создании сущности, id подбирается автоматически, поэтому нет разницы
     * какое число туда подставлять. Здесть подставляется нуль
     */
    @Override
    protected void fillEntity(PreparedStatement statement, User user) throws SQLException {
        fillFields(statement, user);
    }

    @Override
    protected void updateEntity(PreparedStatement statement, User user) throws SQLException {
        fillFields(statement, user);
        statement.setLong(1, user.getId());
        statement.setLong(5, user.getId());
    }

    @Override
    protected void fillUniqueField(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, composeHashCode(user));
    }

    @Override
    protected User extractResultSet(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong(ID_FIELD_NAME),
                resultSet.getString(FIRST_NAME_FIELD_NAME),
                resultSet.getString(SECOND_NAME_FIELD_NAME)
        );
    }

    private void fillFields(PreparedStatement statement, User user) throws SQLException {
        statement.setLong(1, ZERO);
        statement.setString(2, user.getFirstName());
        statement.setString(3, user.getSecondName());
        statement.setString(4, composeHashCode(user));
    }

    private String composeHashCode(User user) {
        return user.getFirstName() + user.getSecondName();
    }
}
