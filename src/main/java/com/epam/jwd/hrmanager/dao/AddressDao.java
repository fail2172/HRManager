package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Address;

import java.util.Optional;

public interface AddressDao extends EntityDao<Address> {

    Long receiveCityId(Address address);

    Long receiveStreetId(Address address);

}
