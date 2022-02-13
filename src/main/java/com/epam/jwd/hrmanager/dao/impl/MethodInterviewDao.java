package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.InterviewDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.model.Interview;
import com.epam.jwd.hrmanager.model.InterviewStatus;
import com.epam.jwd.hrmanager.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MethodInterviewDao extends CommonDao<Interview> implements InterviewDao {

    private static MethodInterviewDao instance;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Logger LOGGER = LogManager.getLogger(MethodInterviewDao.class);
    private static final String INTERVIEW_TABLE_NAME = "interview";
    private static final String ID_FIELD_NAME = "id";
    private static final String DATE_FIELD_NAME = "i_date";
    private static final String TIME_FIELD_NAME = "i_time";
    private static final String ADDRESS_ID_FIELD_NAME = "address_id";
    private static final String USER_ID_FIELD_NAME = "user_id";
    private static final String VACANCY_ID_FIELD_NAME = "vacancy_id";
    private static final String STATUS_FIELD_NAME = "i_status";
    private static final String HASH = "i_hash";
    private static final Integer ZERO = 0;
    private static final List<String> FIELDS = Arrays.asList(
            ID_FIELD_NAME, DATE_FIELD_NAME, TIME_FIELD_NAME, ADDRESS_ID_FIELD_NAME,
            USER_ID_FIELD_NAME, VACANCY_ID_FIELD_NAME, STATUS_FIELD_NAME, HASH
    );

    private MethodInterviewDao(ConnectionPool connectionPool){
        super(LOGGER, connectionPool);
    }

    static MethodInterviewDao getInstance(ConnectionPool connectionPool){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new MethodInterviewDao(connectionPool);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    protected String getTableName() {
        return INTERVIEW_TABLE_NAME;
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
    protected void fillEntity(PreparedStatement statement, Interview interview) throws SQLException {
        fillFields(statement, interview);
    }

    @Override
    protected void updateEntity(PreparedStatement statement, Interview interview) throws SQLException {
        fillFields(statement, interview);
        statement.setLong(1, interview.getId());
        statement.setLong(9, interview.getId());
    }

    @Override
    protected void fillUniqueField(PreparedStatement statement, Interview interview) throws SQLException {
        statement.setString(1, composeHashCode(interview));
    }

    @Override
    protected Interview extractResultSet(ResultSet resultSet) throws SQLException {
        return new Interview(
                resultSet.getLong(ID_FIELD_NAME),
                InterviewStatus.of(resultSet.getString(STATUS_FIELD_NAME)),
                resultSet.getDate(DATE_FIELD_NAME),
                resultSet.getTime(TIME_FIELD_NAME)
        );
    }

    @Override
    public Long receiveAddressId(Interview interview) {
        return ((Number) receiveEntityParam(interview, ADDRESS_ID_FIELD_NAME)).longValue();
    }

    @Override
    public Long receiveUserId(Interview interview) {
        return ((Number) receiveEntityParam(interview, USER_ID_FIELD_NAME)).longValue();
    }

    @Override
    public Long receiveVacancyId(Interview interview) {
        return ((Number) receiveEntityParam(interview, VACANCY_ID_FIELD_NAME)).longValue();
    }

    @Override
    public List<Interview> receiveInterviewsByUser(User user) {
        return receiveEntitiesByParam(USER_ID_FIELD_NAME, user.getId());
    }

    private void fillFields(PreparedStatement statement, Interview interview) throws SQLException {
        statement.setLong(1, ZERO);
        statement.setDate(2, interview.getDate());
        statement.setTime(3, interview.getTime());
        statement.setLong(4, interview.getAddress().getId());
        statement.setLong(5, interview.getUser().getId());
        statement.setLong(6, interview.getVacancy().getId());
        statement.setString(7, interview.getStatus().toString());
        statement.setString(8, composeHashCode(interview));
    }

    private String composeHashCode(Interview interview){
        return String.valueOf(interview.getDate())
                + interview.getTime()
                + interview.getAddress().getId()
                + interview.getUser().getId()
                + interview.getVacancy().getId()
                + interview.getStatus();
    }
}
