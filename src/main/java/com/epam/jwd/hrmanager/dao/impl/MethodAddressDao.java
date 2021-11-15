package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.AddressDao;
import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.db.ConnectionPoolFactory;
import com.epam.jwd.hrmanager.db.ConnectionPoolType;
import com.epam.jwd.hrmanager.model.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MethodAddressDao extends CommonDao<Address> implements AddressDao {

    private static final Logger LOGGER = LogManager.getLogger(MethodAddressDao.class);
    private static final String ADDRESS_TABLE_NAME = "address";
    public static final String ID_FIELD_NAME = "id";
    public static final String HOUSE_NUMBER_FIELD_NAME = "house_number";
    public static final String FLAT_NUMBER_FIELD_NAME = "flat_number";
    public static final String CITY_ID_FIELD_NAME = "city_id";
    public static final String STREET_ID_FIELD_NAME = "street_id";

    private MethodAddressDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    static MethodAddressDao getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    protected String getTableName() {
        return ADDRESS_TABLE_NAME;
    }

    @Override
    protected Address extractResultSet(ResultSet resultSet) throws SQLException {
        return new Address(
                resultSet.getLong(ID_FIELD_NAME),
                resultSet.getInt(HOUSE_NUMBER_FIELD_NAME),
                resultSet.getInt(FLAT_NUMBER_FIELD_NAME),
                null, null
        );
    }

    @Override
    public Optional<Long> receiveCityId(Address address) {
        return receiveForeignKey(address, CITY_ID_FIELD_NAME);
    }

    @Override
    public Optional<Long> receiveStreetId(Address address) {
        return receiveForeignKey(address, STREET_ID_FIELD_NAME);
    }

    private static class Holder {
        private static final MethodAddressDao INSTANCE = new MethodAddressDao(ConnectionPoolFactory.getInstance()
                .getBy(ConnectionPoolType.SIMPLE_CONNECTION_POOL));
    }
}
