package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.InterviewDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;
import com.epam.jwd.hrmanager.model.Interview;
import com.epam.jwd.hrmanager.model.InterviewStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MethodInterviewDao extends CommonDao<Interview> implements InterviewDao {

    private static final Logger LOGGER = LogManager.getLogger(MethodInterviewDao.class);
    private static final String INTERVIEW_TABLE_NAME = "interview i join interview_status s on i.status_id = s.id";
    private static final String ID_FIELD_NAME = "i.id";
    private static final String DATE_FIELD_NAME = "i.i_date";
    private static final String ADDRESS_ID_FIELD_NAME = "i.address_id";
    private static final String USER_ID_FIELD_NAME = "i.user_id";
    private static final String VACANCY_ID_FIELD_NAME = "i.vacancy_id";
    private static final String STATUS_FIELD_NAME = "s.s_name";
    private static final List<String> FIELDS = Arrays.asList(
            ID_FIELD_NAME, DATE_FIELD_NAME, ADDRESS_ID_FIELD_NAME,
            USER_ID_FIELD_NAME, VACANCY_ID_FIELD_NAME, STATUS_FIELD_NAME
    );

    private MethodInterviewDao(ConnectionPool connectionPool){
        super(LOGGER, connectionPool);
    }

    static MethodInterviewDao getInstance(){
        return Holder.INSTANCE;
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
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected Interview extractResultSet(ResultSet resultSet) throws SQLException {
        return new Interview(
                resultSet.getLong(ID_FIELD_NAME),
                InterviewStatus.of(resultSet.getString(STATUS_FIELD_NAME)),
                null,
                null,
                null,
                resultSet.getDate(DATE_FIELD_NAME)
        );
    }

    @Override
    public Optional<Long> receiveAddressId(Interview interview) {
        return receiveForeignKey(interview, ADDRESS_ID_FIELD_NAME);
    }

    @Override
    public Optional<Long> receiveUserId(Interview interview) {
        return receiveForeignKey(interview, USER_ID_FIELD_NAME);
    }

    @Override
    public Optional<Long> receiveVacancyId(Interview interview) {
        return receiveForeignKey(interview, VACANCY_ID_FIELD_NAME);
    }

    private static class Holder {
        private static final MethodInterviewDao INSTANCE = new MethodInterviewDao(ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.TRANSACTION_CONNECTION_POOL));
    }
}
