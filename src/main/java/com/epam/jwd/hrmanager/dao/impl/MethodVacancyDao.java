package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.VacancyDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.model.Vacancy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class MethodVacancyDao extends CommonDao<Vacancy> implements VacancyDao {

    private static MethodVacancyDao instance;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Logger LOGGER = LogManager.getLogger(MethodVacancyDao.class);
    private static final String VACANCY_TABLE_NAME = "vacancy";
    private static final String ID_FIELD_NAME = "id";
    private static final String TITLE_NAME_FIELD = "title";
    private static final String SALARY_FIELD_NAME = "salary";
    private static final String DESCRIPTION_FIELD_NAME = "description";
    private static final String CITY_ID_FIELD_NAME = "city_id";
    private static final String EMPLOYER_ID_FIELD_NAME = "employer_id";
    private static final String HASH = "v_hash";
    private static final String EMPTY_LINE = "";
    private static final Integer ZERO = 0;
    private static final List<String> FIELDS = Arrays.asList(
            ID_FIELD_NAME, TITLE_NAME_FIELD, SALARY_FIELD_NAME,
            DESCRIPTION_FIELD_NAME, CITY_ID_FIELD_NAME, EMPLOYER_ID_FIELD_NAME, HASH
    );


    private MethodVacancyDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    static MethodVacancyDao getInstance(ConnectionPool connectionPool) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new MethodVacancyDao(connectionPool);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    protected String getTableName() {
        return VACANCY_TABLE_NAME;
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
    protected void fillEntity(PreparedStatement statement, Vacancy vacancy) throws SQLException {
        fillFields(statement, vacancy);
    }

    @Override
    protected void updateEntity(PreparedStatement statement, Vacancy vacancy) throws SQLException {
        fillFields(statement, vacancy);
        statement.setLong(1, vacancy.getId());
        statement.setLong(8, vacancy.getId());
    }

    @Override
    protected void fillUniqueField(PreparedStatement statement, Vacancy vacancy) throws SQLException {
        statement.setString(1, composeHashCode(vacancy));
    }

    @Override
    protected Vacancy extractResultSet(ResultSet resultSet) throws SQLException {
        return new Vacancy(
                resultSet.getLong(ID_FIELD_NAME),
                resultSet.getString(TITLE_NAME_FIELD),
                resultSet.getBigDecimal(SALARY_FIELD_NAME),
                resultSet.getString(DESCRIPTION_FIELD_NAME)
        );
    }

    @Override
    public Long receiveEmployerId(Vacancy vacancy) {
        return ((Number) receiveEntityParam(vacancy, EMPLOYER_ID_FIELD_NAME)).longValue();
    }

    @Override
    public Long receiveCityId(Vacancy vacancy) {
        return ((Number) receiveEntityParam(vacancy, CITY_ID_FIELD_NAME)).longValue();
    }

    private void fillFields(PreparedStatement statement, Vacancy vacancy) throws SQLException {
        statement.setLong(1, ZERO);
        statement.setString(2, vacancy.getTitle());
        statement.setBigDecimal(3, vacancy.getSalary());
        statement.setString(4, vacancy.getDescription().orElse(EMPTY_LINE));
        statement.setLong(5, vacancy.getCity().getId());
        statement.setLong(6, vacancy.getEmployer().getId());
        statement.setString(7, composeHashCode(vacancy));
    }

    private String composeHashCode(Vacancy vacancy) {
        return vacancy.getTitle() + vacancy.getSalary() + vacancy.getCity().getId() + vacancy.getEmployer().getId();
    }
}
