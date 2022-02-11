package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.City;

import java.util.Optional;

public interface CityDao extends EntityDao<City> {

    Optional<City> receiveByName(String name);

}
