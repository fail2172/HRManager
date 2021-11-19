package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.AddressDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.model.Address;
import com.epam.jwd.hrmanager.model.City;
import com.epam.jwd.hrmanager.model.Street;
import com.epam.jwd.hrmanager.secvice.EntityService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AddressService implements EntityService<Address> {

    private static AddressService instance;
    private static final ReentrantLock lock = new ReentrantLock();

    private final AddressDao addressDao;
    private final EntityDao<City> cityDao;
    private final EntityDao<Street> streetDao;

    private AddressService(AddressDao addressDao, EntityDao<City> cityDao, EntityDao<Street> streetDao) {
        this.addressDao = addressDao;
        this.cityDao = cityDao;
        this.streetDao = streetDao;
    }

    static AddressService getInstance(AddressDao addressDao, EntityDao<City> cityDao, EntityDao<Street> streetDao){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new AddressService(addressDao, cityDao, streetDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public Address get(Long id) {
        Address address = addressDao.read(id).orElse(null);
        final Long cityId = addressDao.receiveCityId(address).orElse(null);
        final Long streetId = addressDao.receiveStreetId(address).orElse(null);
        final City city = cityDao.read(cityId).orElse(null);
        final Street street = streetDao.read(streetId).orElse(null);
        return Objects.requireNonNull(address).withCity(city).withStreet(street);
    }

    @Override
    public List<Address> findAll() {
        return addressDao.read().stream()
                .map(address -> this.get(address.getId()))
                .collect(Collectors.toList());
    }
}
