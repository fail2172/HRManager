package com.epam.jwd.hrmanager.db;

import com.epam.jwd.hrmanager.exeption.EntityExtractionFailedException;
import com.epam.jwd.hrmanager.exeption.IdExtractionFailedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface ResultSetExtractor<T> {

    T extract(ResultSet resultSet) throws EntityExtractionFailedException;

    default List<T> extractAll(ResultSet resultSet) throws EntityExtractionFailedException, SQLException {
        List<T> entities = new ArrayList<>();
        while (resultSet.next()) {
            final T entity = extract(resultSet);
            entities.add(entity);
        }
        return entities;
    }

}
