package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ResultSetExtractor;
import com.epam.jwd.hrmanager.db.StatementPreparator;
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

import static java.lang.String.format;
import static java.lang.String.join;

public abstract class CommonDao<T extends Entity> implements EntityDao<T> {

    private static final String SELECT_ALL_FROM = "select %s from %s";
    private static final String SELECT_FIELD = "select %s as field from %s";
    private static final String INSERT_INTO = "insert into %s (%s) values(%s)";
    private static final String FIELD_NAME = "field";
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
        T checkEntity = search(selectByUField, ps -> fillUniqueField(ps, t), this::extractResultCheckingException);
        if (checkEntity != null) {
            return checkEntity;
        }
        try {
            update(insertSql, st -> fillEntity(st, t));
        } catch (InterruptedException e) {
            logger.info("take connection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return search(selectByUField, ps -> fillUniqueField(ps, t), this::extractResultCheckingException);
    }

    @Override
    public Optional<T> read(Long id) {
        try {
            return Optional.ofNullable(
                    search(selectByIdExpression, ps -> ps.setLong(1, id), this::extractResultCheckingException)
            );
        } catch (InterruptedException e) {
            logger.info("take connection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    @Override
    public List<T> read() {
        try {
            return searchList(selectAllExpression, st -> {}, this::extractResultCheckingException);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted", e);
            Thread.currentThread().interrupt();
        }
        return Collections.emptyList();
    }

    /**
     * Метод получает сущность с её реальным id и проверяет есть ли такая в базе.
     * Далее он обновляет все поля сущности.
     * Если оказалось так, что новая обновлённая сущность является копией уже существующей,
     * то обновление не будет произведено, а просто вернётся сущность которая уже существовала
     * в базе(с другим идентификатором)
     */
    @Override
    public T update(T t) throws InterruptedException, EntityUpdateException, NotFoundEntityException {
        try {
            T checkEntity = search(selectByIdExpression,
                    ps -> ps.setLong(1, t.getId()), this::extractResultCheckingException);
            if (checkEntity == null) {
                throw new NotFoundEntityException(format("Entity with id=%s not found", t.getId()));
            }
            update(updateExpression, ps -> updateEntity(ps, t));
            return search(selectByUField, st -> fillUniqueField(st, t), this::extractResultCheckingException);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            final int updatedLines = update(deleteExpression, ps -> ps.setLong(1, id));
            return updatedLines > 0;
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private <R> R search(String sql, StatementPreparator statementPreparation,
                                   ResultSetExtractor<R> extractor) throws InterruptedException {
        try (Connection conn = connectionPool.takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            statementPreparation.accept(statement);
            final ResultSet resultSet = statement.executeQuery();
            return resultSet.next()
                    ? extractor.extract(resultSet)
                    : null;
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            throw e;
        } catch (EntityExtractionFailedException e) {
            logger.error("could not extract entity", e);
        }
        return null;
    }

    private <R> List<R> searchList(String sql, StatementPreparator statementPreparation,
                                   ResultSetExtractor<R> extractor) throws InterruptedException {
        try (Connection conn = connectionPool.takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            statementPreparation.accept(statement);
            final ResultSet resultSet = statement.executeQuery();
            return extractor.extractAll(resultSet);
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
        } catch (InterruptedException e) {
            logger.warn("take connection interrupted");
            Thread.currentThread().interrupt();
            throw e;
        } catch (EntityExtractionFailedException e) {
            logger.error("could not extract entity", e);
        }
        return Collections.emptyList();
    }

    private int update(String sql, StatementPreparator statementPreparation) throws InterruptedException {
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

    private Long extractForeignKey(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return resultSet.getLong(FIELD_NAME);
        } catch (SQLException e) {
            logger.error("sql exception occurred extracting entity from ResultSet", e);
            throw new EntityExtractionFailedException("failed to extract entity parameter id", e);
        }
    }

    private Object extractField(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return resultSet.getObject(FIELD_NAME);
        } catch (SQLException e) {
            logger.error("sql exception occurred extracting entity from ResultSet", e);
            throw new EntityExtractionFailedException("failed to extract entity parameter id", e);
        }
    }

    protected Object receiveEntityParam(Entity entity, String fieldName){
        try {
            final String selectField = format(SELECT_FIELD, fieldName, getTableName()) + SPACE + format(WHERE_FIELD, getIdFieldName());
            return search(selectField, st -> st.setLong(1, entity.getId()), this::extractField);
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
