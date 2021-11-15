package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.dao.VacancyDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;
import com.epam.jwd.hrmanager.model.Vacancy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MethodVacancyDao extends CommonDao<Vacancy> implements VacancyDao {

    private static final Logger LOGGER = LogManager.getLogger(MethodVacancyDao.class);
    private static final String VACANCY_TABLE_NAME = "vacancy";
    private static final String ID_FIELD_NAME = "id";
    private static final String TITLE_NAME_FIELD = "title";
    private static final String SALARY_FIELD_NAME = "salary";
    private static final String DESCRIPTION_FIELD_NAME = "description";
    public static final String CITY_ID_FIELD_NAME = "city_id";
    public static final String EMPLOYER_ID_FIELD_NAME = "employer_id";

    private MethodVacancyDao(ConnectionPool connectionPool){
        super(LOGGER, connectionPool);
    }

    static MethodVacancyDao getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    protected String getTableName() {
        return VACANCY_TABLE_NAME;
    }

    @Override
    protected Vacancy extractResultSet(ResultSet resultSet) throws SQLException {
        return new Vacancy(
                resultSet.getLong(ID_FIELD_NAME),
                resultSet.getString(TITLE_NAME_FIELD),
                resultSet.getBigDecimal(SALARY_FIELD_NAME),
                null,
                null,
                resultSet.getString(DESCRIPTION_FIELD_NAME)
        );
    }

    @Override
    public Optional<Long> receiveEmployerId(Vacancy vacancy) {
        return receiveForeignKey(vacancy, EMPLOYER_ID_FIELD_NAME);
    }

    @Override
    public Optional<Long> receiveCityId(Vacancy vacancy) {
        return receiveForeignKey(vacancy, CITY_ID_FIELD_NAME);
    }

    private static class Holder {
        private static final MethodVacancyDao INSTANCE = new MethodVacancyDao(ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.TRANSACTION_CONNECTION_POOL));
    }
}
