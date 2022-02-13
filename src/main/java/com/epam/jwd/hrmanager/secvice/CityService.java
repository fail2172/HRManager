package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.model.City;

import java.util.Optional;

public interface CityService extends EntityService<City> {

    Optional<City> receiveByName(String name);

}
