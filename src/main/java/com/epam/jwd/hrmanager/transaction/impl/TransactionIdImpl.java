package com.epam.jwd.hrmanager.transaction.impl;

import com.epam.jwd.hrmanager.transaction.TransactionId;

import java.sql.Connection;

public class TransactionIdImpl implements TransactionId {

    private final Connection connection;

    TransactionIdImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionIdImpl that = (TransactionIdImpl) o;

        return connection.equals(that.connection);
    }

    @Override
    public int hashCode() {
        return connection.hashCode();
    }

    @Override
    public String toString() {
        return "TransactionIdImpl{" +
                "connection=" + connection +
                '}';
    }
}
