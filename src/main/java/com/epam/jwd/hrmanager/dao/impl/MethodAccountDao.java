package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.AccountDao;
import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.model.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class MethodAccountDao extends CommonDao<Account> implements AccountDao {

    private static MethodAccountDao instance;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Logger LOGGER = LogManager.getLogger(MethodAccountDao.class);
    private static final String ACCOUNT_TABLE_NAME = "account";
    private static final String ID_FIELD_NAME = "id";
    private static final String USER_ID_FIELD_NAME = "user_id";
    private static final String LOGIN_FIELD_NAME = "login";
    private static final String EMAIL_FIELD_NAME = "email";
    private static final String PASSWORD_FIELD_NAME = "a_password";
    private static final Integer ZERO = 0;
    private static final List<String> FIELDS = Arrays.asList(
            ID_FIELD_NAME, USER_ID_FIELD_NAME,
            LOGIN_FIELD_NAME, EMAIL_FIELD_NAME,
            PASSWORD_FIELD_NAME
    );

    private MethodAccountDao(ConnectionPool connectionPool){
        super(LOGGER, connectionPool);
    }

    static MethodAccountDao getInstance(ConnectionPool connectionPool){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new MethodAccountDao(connectionPool);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public Long receiveUserId(Account account) {
        return ((Number) receiveEntityParam(account, USER_ID_FIELD_NAME)).longValue();
    }

    @Override
    public Optional<Account> receiveAccountByLogin(String email) {
        return receiveEntityByParam(LOGIN_FIELD_NAME, email);
    }

    @Override
    protected String getTableName() {
        return ACCOUNT_TABLE_NAME;
    }

    @Override
    protected String getIdFieldName() {
        return ID_FIELD_NAME;
    }

    @Override
    protected String getUniqueFieldName() {
        return LOGIN_FIELD_NAME;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    /**
     * При создании сущности, id подбирается автоматически, поэтому нет разницы
     * какое число туда подставлять. Здесь подставляется нуль
     */
    @Override
    protected void fillEntity(PreparedStatement statement, Account account) throws SQLException {
        fillFields(statement, account);
    }

    @Override
    protected void updateEntity(PreparedStatement statement, Account account) throws SQLException {
        fillFields(statement, account);
        statement.setLong(1, account.getId());
        statement.setLong(6, account.getId());
    }

    @Override
    protected void fillUniqueField(PreparedStatement statement, Account account) throws SQLException {
        statement.setString(1, account.getLogin());
    }

    @Override
    protected Account extractResultSet(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getLong(ID_FIELD_NAME),
                resultSet.getString(LOGIN_FIELD_NAME),
                resultSet.getString(EMAIL_FIELD_NAME),
                resultSet.getString(PASSWORD_FIELD_NAME),
                null
        );
    }

    private void fillFields(PreparedStatement statement, Account account) throws SQLException {
        statement.setLong(1, ZERO);
        statement.setLong(2, account.getUser().getId());
        statement.setString(3, account.getLogin());
        statement.setString(4, account.getEmail());
        statement.setString(5, account.getPassword());
    }
}
