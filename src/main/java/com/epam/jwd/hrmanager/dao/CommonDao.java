package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ResultSetContext;
import com.epam.jwd.hrmanager.db.ResultSetExtractor;
import com.epam.jwd.hrmanager.db.StatementPreparator;
import com.epam.jwd.hrmanager.db.impl.MultipleContext;
import com.epam.jwd.hrmanager.db.impl.SingleContext;
import com.epam.jwd.hrmanager.exeption.EntityExtractionFailedException;
import com.epam.jwd.hrmanager.model.Entity;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class CommonDao<T extends Entity> implements EntityDao<T> {

    public static final String SELECT_ALL_FROM = "select * from ";
    private final Logger logger;
    protected final ConnectionPool connectionPool;

    protected CommonDao(Logger logger, ConnectionPool connectionPool) {
        this.logger = logger;
        this.connectionPool = connectionPool;
    }

    @Override
    public T create() {
        return null;
    }

    @Override
    public Optional<T> read(Long id) {
        try {
            return searchParameterizedEntity(
                    SELECT_ALL_FROM + getTableName() + " where id = ?",
                    this::extractResultCheckingException,
                    preparedStatement -> preparedStatement.setLong(1, id)
            );
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    @Override
    public List<T> read() {
        try {
            return searchEntityList(SELECT_ALL_FROM + getTableName(), this::extractResultCheckingException);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        }
        return Collections.emptyList();
    }

    @Override
    public T update(T t) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    protected Optional<T> searchEntity(String sql, ResultSetExtractor<T> extractor) throws InterruptedException {
        SingleContext<T> context = (SingleContext<T>) (executeStatement(sql, receiveSingleExtraction(extractor)))
                .orElse(ResultSetContext.single(null));
        return context.getContext();
    }

    protected List<T> searchEntityList(String sql, ResultSetExtractor<T> extractor) throws InterruptedException {
        MultipleContext<T> context = (MultipleContext<T>) executeStatement(sql, receiveMultipleExtraction(sql, extractor))
                .orElse(ResultSetContext.multiple(Collections.emptyList()));
        return context.getContext();
    }

    protected Optional<T> searchParameterizedEntity(String sql,
                                                    ResultSetExtractor<T> extractor,
                                                    StatementPreparator statementPreparation) throws InterruptedException {
        SingleContext<T> context = (SingleContext<T>) (executePrepared(sql, statementPreparation,
                receiveSingleExtraction(extractor))).orElse(ResultSetContext.single(null));
        return context.getContext();
    }

    protected List<T> searchParameterizedEntityList(String sql,
                                                    ResultSetExtractor<T> extractor,
                                                    StatementPreparator statementPreparation) throws InterruptedException {
        MultipleContext<T> context = (MultipleContext<T>) executePrepared(sql, statementPreparation,
                receiveMultipleExtraction(sql, extractor)).orElse(ResultSetContext.multiple(Collections.emptyList()));
        return context.getContext();
    }

    protected <R> Optional<R> entityParameter(String sql, StatementPreparator statementPreparation,
                                              ResultSetExtractor<R> extractor) throws InterruptedException {
        try (Connection conn = connectionPool.takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            statementPreparation.accept(statement);
            final ResultSet resultSet = statement.executeQuery();
            return resultSet.next()
                    ? Optional.ofNullable(extractor.extract(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            throw e;
        } catch (EntityExtractionFailedException e) {
            logger.error("could not extract entity", e);
        }
        return Optional.empty();
    }

    protected Optional<Long> receiveForeignKey(Entity entity, String fieldName) {
        try {
            return entityParameter("select " + fieldName + " as id from " + getTableName() + " where id = ?",
                    statement -> statement.setLong(1, entity.getId()), this::extractForeignKey);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        }
        return Optional.empty();
    }

    private Long extractForeignKey(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return resultSet.getLong("id");
        } catch (SQLException e) {
            logger.error("sql exception occurred extracting entity from ResultSet", e);
            throw new EntityExtractionFailedException("failed to extract entity parameter id", e);
        }
    }

    private Function<ResultSet, Optional<ResultSetContext<T>>> receiveSingleExtraction(ResultSetExtractor<T> extractor) {
        return (resultSet -> {
            try {
                return resultSet.next()
                        ? Optional.of(ResultSetContext.single(extractor.extract(resultSet)))
                        : Optional.empty();
            } catch (EntityExtractionFailedException e) {
                logger.error("could not extract entity", e);
            } catch (SQLException e) {
                logger.error("sql exception occurred", e);
            }
            return Optional.empty();
        });
    }

    private Function<ResultSet, Optional<ResultSetContext<T>>> receiveMultipleExtraction(String sql, ResultSetExtractor<T> extractor) {
        return (resultSet -> {
            try {
                return Optional.of(ResultSetContext.multiple(extractor.extractAll(resultSet)));
            } catch (EntityExtractionFailedException e) {
                logger.error("could not extract entity", e);
            } catch (SQLException e) {
                logger.error("sql exception occurred", e);
            }
            return Optional.empty();
        });
    }

    private Optional<ResultSetContext<T>> executePrepared(String sql,
                                                          StatementPreparator statementPreparation,
                                                          Function<ResultSet, Optional<ResultSetContext<T>>> extraction) throws InterruptedException {
        try (Connection conn = connectionPool.takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            statementPreparation.accept(statement);
            final ResultSet resultSet = statement.executeQuery();
            return extraction.apply(resultSet);
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            throw e;
        }
        return Optional.empty();
    }

    private Optional<ResultSetContext<T>> executeStatement(String sql, Function<ResultSet, Optional<ResultSetContext<T>>> extraction) throws InterruptedException {
        try (Connection conn = connectionPool.takeConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(sql)) {
            return extraction.apply(resultSet);
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            throw e;
        }
        return Optional.empty();
    }

    private T extractResultCheckingException(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return extractResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("sql exception occurred extraction user from resultSet");
            throw new EntityExtractionFailedException("failed to extract entity", e);
        }
    }

    protected abstract String getTableName();

    protected abstract T extractResultSet(ResultSet resultSet) throws SQLException;

}
