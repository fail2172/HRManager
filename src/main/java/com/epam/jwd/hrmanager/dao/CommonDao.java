package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ResultSetExtractor;
import com.epam.jwd.hrmanager.db.StatementPreparator;
import com.epam.jwd.hrmanager.exeption.EntityExtractionFailedException;
import com.epam.jwd.hrmanager.model.Entity;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Collections;
import java.util.List;

public abstract class CommonDao<T extends Entity> implements EntityDao<T> {

    public static final String SELECT_ALL_FROM = "select * from ";
    private final Logger logger;
    private final String selectAllEntity;
    protected final ConnectionPool connectionPool;

    protected CommonDao(Logger logger, ConnectionPool connectionPool) {
        this.logger = logger;
        this.connectionPool = connectionPool;
        this.selectAllEntity = SELECT_ALL_FROM + getTableName();
    }

    @Override
    public List<T> read() {
        try {
            return executeStatement(selectAllEntity, this::extractResultSet);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        }
        return Collections.emptyList();
    }

    public List<T> executePrepared(String sql, ResultSetExtractor<T> extractor,
                                   StatementPreparator statementPreparation) throws InterruptedException {
        try (Connection conn = connectionPool.takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {

            statementPreparation.accept(statement);
            final ResultSet resultSet = statement.executeQuery();

            return extractor.extractAll(resultSet);

        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
            logger.debug("sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            logger.error("could not extract exception", e);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            throw e;
        }
        return Collections.emptyList();
    }

    public List<T> executeStatement(String sql, ResultSetExtractor<T> extractor) throws InterruptedException {
        try (Connection conn = connectionPool.takeConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(sql)) {

            return extractor.extractAll(resultSet);

        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
            logger.debug("sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            logger.error("could not extract exception", e);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            throw e;
        }
        return Collections.emptyList();
    }

    protected abstract String getTableName();

    protected abstract T extractResultSet(ResultSet resultSet) throws EntityExtractionFailedException;

}
