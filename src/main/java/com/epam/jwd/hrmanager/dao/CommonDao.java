package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ResultSetContext;
import com.epam.jwd.hrmanager.db.ResultSetExtractor;
import com.epam.jwd.hrmanager.db.StatementPreparator;
import com.epam.jwd.hrmanager.db.impl.MultipleContext;
import com.epam.jwd.hrmanager.db.impl.SingleContext;
import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.exeption.EntityExtractionFailedException;
import com.epam.jwd.hrmanager.exeption.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.Entity;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;
import static java.lang.String.join;

public abstract class CommonDao<T extends Entity> implements EntityDao<T> {

    private static final String SELECT_ALL_FROM = "select %s from %s";
    private static final String SELECT_FOREIGN_KEY = "select %s as foreignKey from";
    private static final String INSERT_INTO = "insert into %s (%s) values(%s)";
    private static final String FOREIGN_KEY_NAME = "foreignKey";
    private static final String WHERE_FIELD = "where %s = ?";
    private static final String UPDATE_SET = "update %s set %s";
    private static final String DELETE_FROM = "delete from %s";
    private static final String COMMA = ", ";
    private static final String SPACE = " ";

    private final Logger logger;
    private final String selectByUField;
    private final String selectByIdExpression;
    private final String selectAllExpression;
    private final String insertSql;
    private final String updateExpression;
    private final String deleteExpression;

    protected final ConnectionPool connectionPool;

    protected CommonDao(Logger logger, ConnectionPool connectionPool) {
        this.logger = logger;
        this.connectionPool = connectionPool;
        this.selectAllExpression = format(SELECT_ALL_FROM, join(COMMA, getFields()), getTableName());
        this.selectByIdExpression = this.selectAllExpression + SPACE + format(WHERE_FIELD, getIdFieldName());
        this.insertSql = format(INSERT_INTO, getTableName(), join(COMMA, getFields()), join(COMMA, insertParams()));
        this.selectByUField = this.selectAllExpression + SPACE + format(WHERE_FIELD, getUniqueFieldName());
        this.updateExpression = format(UPDATE_SET, getTableName(), join(COMMA, updateParams())) + SPACE + format(WHERE_FIELD, getIdFieldName());
        this.deleteExpression = format(DELETE_FROM, getTableName()) + SPACE + format(WHERE_FIELD, getIdFieldName());
    }

    private List<String> insertParams() {
        final List<String> parameters = new ArrayList<>();
        getFields().forEach(param -> parameters.add("?"));
        return parameters;
    }

    private List<String> updateParams() {
        final List<String> parameters = new ArrayList<>();
        getFields().forEach(param -> parameters.add(param + "=?"));
        return parameters;
    }

    /**
     * Перед тем как добавить сущность, метод проверяет нет ли таковой в базе.
     * Если есть, то он сразу вернёт данную сущность. А если нет, то сначала метод
     * добавит её в базу, а затем сразу вернйт по уникальному полю
     */
    @Override
    public T create(T t) throws EntityUpdateException, InterruptedException {
        T checkEntity = search(selectByUField, ps -> fillUniqueField(ps, t), this::extractResultCheckingException)
                .orElse(null);
        if (checkEntity != null) {
            return checkEntity;
        }
        try {
            executePreparedUpdate(insertSql, st -> fillEntity(st, t));
        } catch (InterruptedException e) {
            logger.info("take connection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return search(selectByUField, ps -> fillUniqueField(ps, t), this::extractResultCheckingException)
                .orElseThrow(() -> new EntityUpdateException("Unable to add changes to database"));
    }

    @Override
    public Optional<T> read(Long id) {
        try {
            return searchParameterizedEntity(selectByIdExpression,
                    this::extractResultCheckingException, ps -> ps.setLong(1, id));
        } catch (InterruptedException e) {
            logger.info("take connection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    @Override
    public List<T> read() {
        try {
            return searchEntityList(selectAllExpression, this::extractResultCheckingException);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted", e);
            Thread.currentThread().interrupt();
        }
        return Collections.emptyList();
    }

    /**
     * Метод получает сущность с её реальным id и проверяет есть ли такой в базе.
     * Далее он обновляет все поля сущности.
     * Если оказалось так, что новая обновлённая сущность является копией уже существующей,
     * то обновление не будет произведено, а просто вернётся сущность которая уже существовала
     * в базе(с другим идентификатором)
     */
    @Override
    public T update(T t) throws InterruptedException, EntityUpdateException, NotFoundEntityException {
        try {
            search(selectByIdExpression, ps -> ps.setLong(1, t.getId()), this::extractResultCheckingException)
                    .orElseThrow(() -> new NotFoundEntityException("This entity is not in the database"));
            executePreparedUpdate(updateExpression, ps -> updateEntity(ps, t));
            return search(selectByUField, st -> fillUniqueField(st, t), this::extractResultCheckingException).orElseThrow(() -> new EntityUpdateException("Unable to add changes to database"));
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            final int updatedLines = executePreparedUpdate(deleteExpression, ps -> ps.setLong(1, id));
            return updatedLines > 0;
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private Long extractForeignKey(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return resultSet.getLong(FOREIGN_KEY_NAME);
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

    private Function<ResultSet, Optional<ResultSetContext<T>>> receiveMultipleExtraction(ResultSetExtractor<T> extractor) {
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

    private Optional<ResultSetContext<T>> executePreparedStatement(String sql, StatementPreparator statementPreparation,
                                                                   Function<ResultSet, Optional<ResultSetContext<T>>> extraction)
            throws InterruptedException {
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

    private Optional<ResultSetContext<T>> executeStatement(String sql, Function<ResultSet,
            Optional<ResultSetContext<T>>> extraction) throws InterruptedException {
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

    private int executePreparedUpdate(String sql, StatementPreparator statementPreparation) throws InterruptedException {
        try (Connection conn = connectionPool.takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            statementPreparation.accept(statement);
            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            throw e;
        }
        return 0;
    }

    private T extractResultCheckingException(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return extractResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("sql exception occurred extraction user from resultSet");
            throw new EntityExtractionFailedException("failed to extract entity", e);
        }
    }

    protected Optional<T> searchEntity(String sql, ResultSetExtractor<T> extractor) throws InterruptedException {
        SingleContext<T> context = (SingleContext<T>) (executeStatement(sql, receiveSingleExtraction(extractor)))
                .orElse(ResultSetContext.single(null));
        return context.getContext();
    }

    protected List<T> searchEntityList(String sql, ResultSetExtractor<T> extractor) throws InterruptedException {
        MultipleContext<T> context = (MultipleContext<T>) executeStatement(sql, receiveMultipleExtraction(extractor))
                .orElse(ResultSetContext.multiple(Collections.emptyList()));
        return context.getContext();
    }

    protected Optional<T> searchParameterizedEntity(String sql,
                                                    ResultSetExtractor<T> extractor,
                                                    StatementPreparator statementPreparation) throws InterruptedException {
        SingleContext<T> context = (SingleContext<T>) (executePreparedStatement(sql, statementPreparation,
                receiveSingleExtraction(extractor))).orElse(ResultSetContext.single(null));
        return context.getContext();
    }

    protected List<T> searchParameterizedEntityList(String sql,
                                                    ResultSetExtractor<T> extractor,
                                                    StatementPreparator statementPreparation) throws InterruptedException {
        MultipleContext<T> context = (MultipleContext<T>) executePreparedStatement(sql, statementPreparation,
                receiveMultipleExtraction(extractor)).orElse(ResultSetContext.multiple(Collections.emptyList()));
        return context.getContext();
    }

    protected <R> Optional<R> search(String sql, StatementPreparator statementPreparation,
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
            return search(format(SELECT_FOREIGN_KEY, fieldName) + SPACE
                            + getTableName() + SPACE + format(WHERE_FIELD, getIdFieldName()),
                    statement -> statement.setLong(1, entity.getId()), this::extractForeignKey);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        }
        return Optional.empty();
    }

    protected abstract String getTableName();

    protected abstract String getIdFieldName();

    protected abstract String getUniqueFieldName();

    protected abstract List<String> getFields();

    protected abstract void fillEntity(PreparedStatement statement, T entity) throws SQLException;

    protected abstract void updateEntity(PreparedStatement statement, T entity) throws SQLException;

    protected abstract void fillUniqueField(PreparedStatement statement, T entity) throws SQLException;

    protected abstract T extractResultSet(ResultSet resultSet) throws SQLException;

}
