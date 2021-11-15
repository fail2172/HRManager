package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Address;

import java.util.Optional;

public interface AddressDao extends EntityDao<Address> {

    Optional<Long> receiveCityId(Address address);

    Optional<Long> receiveStreetId(Address address);

}
