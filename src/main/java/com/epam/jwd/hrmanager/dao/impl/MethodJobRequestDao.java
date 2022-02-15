package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.JobRequestDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.JobRequest;
import com.epam.jwd.hrmanager.model.JobRequestStatus;
import com.epam.jwd.hrmanager.model.Vacancy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MethodJobRequestDao extends CommonDao<JobRequest> implements JobRequestDao {

    private static final String VACANCY_REQUEST_TABLE_NAME = "job_request";
    private static final String ID_FIELD_NAME = "id";
    private static final String VACANCY_ID_FIELD_NAME = "vacancy_id";
    private static final String ACCOUNT_ID_FIELD_NAME = "account_id";
    private static final String VR_STATUS_FIELD_NAME = "vr_status";
    private static final String HASH = "vr_hash";

    private static final Logger LOGGER = LogManager.getLogger(MethodJobRequestDao.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Integer ZERO = 0;
    private static MethodJobRequestDao instance;
    private static final List<String> FIELDS = Arrays.asList(
            ID_FIELD_NAME, VACANCY_ID_FIELD_NAME, ACCOUNT_ID_FIELD_NAME, VR_STATUS_FIELD_NAME, HASH
    );

    private MethodJobRequestDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    static MethodJobRequestDao getInstance(ConnectionPool connectionPool) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new MethodJobRequestDao(connectionPool);
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
    protected void fillEntity(PreparedStatement statement, JobRequest jobRequest) throws SQLException {
        fillFields(statement, jobRequest);
    }

    @Override
    protected void updateEntity(PreparedStatement statement, JobRequest jobRequest) throws SQLException {
        fillFields(statement, jobRequest);
        statement.setLong(1, jobRequest.getId());
        statement.setLong(6, jobRequest.getId());
    }

    @Override
    protected void fillUniqueField(PreparedStatement statement, JobRequest jobRequest) throws SQLException {
        statement.setString(1, composeHashCode(jobRequest));
    }

    @Override
    protected JobRequest extractResultSet(ResultSet resultSet) throws SQLException {
        return new JobRequest(
                resultSet.getLong(ID_FIELD_NAME),
                JobRequestStatus.of(resultSet.getString(VR_STATUS_FIELD_NAME))
        );
    }

    @Override
    public Long receiveVacancyId(JobRequest jobRequest) {
        return ((Number) receiveEntityParam(jobRequest, VACANCY_ID_FIELD_NAME)).longValue();
    }

    @Override
    public Long receiveAccountId(JobRequest jobRequest) {
        return ((Number) receiveEntityParam(jobRequest, ACCOUNT_ID_FIELD_NAME)).longValue();
    }

    @Override
    public List<JobRequest> receiveJobRequestsByAccount(Account account) {
        return receiveEntitiesByParam(ACCOUNT_ID_FIELD_NAME, account.getId());
    }

    @Override
    public List<JobRequest> receiveJobRequestsByVacancy(Vacancy vacancy) {
        return receiveEntitiesByParam(VACANCY_ID_FIELD_NAME, vacancy.getId());
    }

    private void fillFields(PreparedStatement statement, JobRequest jobRequest) throws SQLException {
        statement.setLong(1, ZERO);
        statement.setLong(2, jobRequest.getVacancy().getId());
        statement.setLong(3, jobRequest.getAccount().getId());
        statement.setString(4, jobRequest.getStatus().name());
        statement.setString(5, composeHashCode(jobRequest));
    }

    private String composeHashCode(JobRequest jobRequest) {
        return jobRequest.getVacancy().getId()
                + jobRequest.getAccount().getId()
                + jobRequest.getStatus().name();
    }
}
