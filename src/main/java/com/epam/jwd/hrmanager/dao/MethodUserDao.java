package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.exeption.EntityExtractionFailedException;
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
    private static final String LOGIN_FIELD_NAME = "login";
    private static final String EMAIL_FIELD_NAME = "email";
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String SECOND_NAME_FIELD_NAME = "second_name";

    private MethodUserDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    static MethodUserDao getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public User create() {
        return null;
    }

    @Override
    public Optional<User> read(Long id) {
        return Optional.empty();
    }

    @Override
    protected String getTableName() {
        return USER_TABLE_NAME;
    }

    @Override
    protected User extractResultSet(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return new User(
                    resultSet.getLong(ID_FIELD_NAME),
                    resultSet.getString(LOGIN_FIELD_NAME),
                    resultSet.getString(EMAIL_FIELD_NAME)
            );
        } catch (SQLException e) {
            LOGGER.error("sql exception occurred extraction user from resultSet");
            throw new EntityExtractionFailedException("failed to extract user");
        }
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    private static class Holder {
        public static final MethodUserDao INSTANCE = new MethodUserDao(ConnectionPool.getInstance());
    }

}
