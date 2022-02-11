package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.VacancyRequestDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.model.Interview;
import com.epam.jwd.hrmanager.model.VacancyRequest;
import com.epam.jwd.hrmanager.model.VacancyRequestStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MethodVacancyRequestDao extends CommonDao<VacancyRequest> implements VacancyRequestDao {

    private static MethodVacancyRequestDao instance;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Logger LOGGER = LogManager.getLogger(MethodVacancyRequestDao.class);
    private static final String VACANCY_REQUEST_TABLE_NAME = "vacancy_request";
    private static final String ID_FIELD_NAME = "id";
    private static final String VACANCY_ID_FIELD_NAME = "vacancy_id";
    private static final String ACCOUNT_ID_FIELD_NAME = "account_id";
    private static final String VR_STATUS_FIELD_NAME = "vr_status";
    private static final String HASH = "vr_hash";
    private static final Integer ZERO = 0;
    private static final List<String> FIELDS = Arrays.asList(
            ID_FIELD_NAME, VACANCY_ID_FIELD_NAME, ACCOUNT_ID_FIELD_NAME, VR_STATUS_FIELD_NAME, HASH
    );

    private MethodVacancyRequestDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    static MethodVacancyRequestDao getInstance(ConnectionPool connectionPool) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new MethodVacancyRequestDao(connectionPool);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    protected String getTableName() {
        return VACANCY_REQUEST_TABLE_NAME;
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

    @Override
    protected void fillEntity(PreparedStatement statement, VacancyRequest vacancyRequest) throws SQLException {
        fillFields(statement, vacancyRequest);
    }

    @Override
    protected void updateEntity(PreparedStatement statement, VacancyRequest vacancyRequest) throws SQLException {
        fillFields(statement, vacancyRequest);
        statement.setLong(1, vacancyRequest.getId());
        statement.setLong(6, vacancyRequest.getId());
    }

    @Override
    protected void fillUniqueField(PreparedStatement statement, VacancyRequest vacancyRequest) throws SQLException {
        statement.setString(1, composeHashCode(vacancyRequest));
    }

    @Override
    protected VacancyRequest extractResultSet(ResultSet resultSet) throws SQLException {
        return new VacancyRequest(
                resultSet.getLong(ID_FIELD_NAME),
                VacancyRequestStatus.of(resultSet.getString(VR_STATUS_FIELD_NAME))
        );
    }

    @Override
    public Long receiveVacancyId(VacancyRequest vacancyRequest) {
        return ((Number) receiveEntityParam(vacancyRequest, VACANCY_ID_FIELD_NAME)).longValue();
    }

    @Override
    public Long receiveAccountId(VacancyRequest vacancyRequest) {
        return ((Number) receiveEntityParam(vacancyRequest, ACCOUNT_ID_FIELD_NAME)).longValue();
    }

    private void fillFields(PreparedStatement statement, VacancyRequest vacancyRequest) throws SQLException {
        statement.setLong(1, ZERO);
        statement.setLong(2, vacancyRequest.getVacancy().getId());
        statement.setLong(3, vacancyRequest.getAccount().getId());
        statement.setString(4, vacancyRequest.getStatus().name());
        statement.setString(5, composeHashCode(vacancyRequest));
    }

    private String composeHashCode(VacancyRequest vacancyRequest) {
        return vacancyRequest.getVacancy().getId()
                + vacancyRequest.getAccount().getId()
                + vacancyRequest.getStatus().name();
    }
}
