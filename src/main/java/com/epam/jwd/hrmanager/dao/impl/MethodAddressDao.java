package com.epam.jwd.hrmanager.dao.impl;

import com.epam.jwd.hrmanager.dao.AddressDao;
import com.epam.jwd.hrmanager.dao.CommonDao;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import com.epam.jwd.hrmanager.model.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.String.join;

public class MethodAddressDao extends CommonDao<Address> implements AddressDao {

    private static MethodAddressDao instance;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Logger LOGGER = LogManager.getLogger(MethodAddressDao.class);
    private static final String ADDRESS_TABLE_NAME = "address";
    private static final String ID_FIELD_NAME = "id";
    private static final String HOUSE_NUMBER_FIELD_NAME = "house_number";
    private static final String FLAT_NUMBER_FIELD_NAME = "flat_number";
    private static final String CITY_ID_FIELD_NAME = "city_id";
    private static final String STREET_ID_FIELD_NAME = "street_id";
    private static final String HASH = "a_hash";
    private static final Integer ZERO = 0;
    private static final List<String> FIELDS = Arrays.asList(
            ID_FIELD_NAME, CITY_ID_FIELD_NAME, STREET_ID_FIELD_NAME,
            HOUSE_NUMBER_FIELD_NAME, FLAT_NUMBER_FIELD_NAME, HASH
    );

    private MethodAddressDao(ConnectionPool connectionPool) {
        super(LOGGER, connectionPool);
    }

    static MethodAddressDao getInstance(ConnectionPool connectionPool) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new MethodAddressDao(connectionPool);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    protected String getTableName() {
        return ADDRESS_TABLE_NAME;
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
    protected void fillEntity(PreparedStatement statement, Address address) throws SQLException {
        fillFields(statement, address);
    }

    @Override
    protected void updateEntity(PreparedStatement statement, Address address) throws SQLException {
        fillFields(statement, address);
        statement.setLong(1, address.getId());
        statement.setLong(7, address.getId());
    }

    @Override
    protected void fillUniqueField(PreparedStatement statement, Address address) throws SQLException {
        statement.setString(1, composeHashCode(address));
    }

    @Override
    protected Address extractResultSet(ResultSet resultSet) throws SQLException {
        return new Address(
                resultSet.getLong(ID_FIELD_NAME),
                null, null,
                resultSet.getInt(HOUSE_NUMBER_FIELD_NAME),
                resultSet.getInt(FLAT_NUMBER_FIELD_NAME)
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

    private String composeHashCode(Address address) {
        return String.valueOf(address.getCity().getId()) +
                address.getStreet().getId() +
                address.getHouseNumber() +
                address.getFlatNumber();
    }

    private void fillFields(PreparedStatement statement, Address address) throws SQLException {
        statement.setLong(1, ZERO);
        statement.setLong(2, address.getCity().getId());
        statement.setLong(3, address.getStreet().getId());
        statement.setInt(4, address.getHouseNumber());
        statement.setInt(5, address.getFlatNumber().orElse(0));
        statement.setString(6, composeHashCode(address));
    }
}
