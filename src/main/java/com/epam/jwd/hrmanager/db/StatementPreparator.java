package com.epam.jwd.hrmanager.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementPreparator {

    void accept(PreparedStatement preparedStatement) throws SQLException;

}
