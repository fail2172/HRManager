package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.AddressDao;
import com.epam.jwd.hrmanager.dao.CityDao;
import com.epam.jwd.hrmanager.dao.StreetDao;
import com.epam.jwd.hrmanager.exception.EntityUpdateException;
import com.epam.jwd.hrmanager.exception.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.Address;
import com.epam.jwd.hrmanager.model.City;
import com.epam.jwd.hrmanager.model.Street;
import com.epam.jwd.hrmanager.secvice.AddressService;
import com.epam.jwd.hrmanager.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AddressServiceImpl implements AddressService {

    private static final Logger LOGGER = LogManager.getLogger(AddressServiceImpl.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static AddressServiceImpl instance;

    private final AddressDao addressDao;
    private final CityDao cityDao;
    private final StreetDao streetDao;

    private AddressServiceImpl(AddressDao addressDao, CityDao cityDao, StreetDao streetDao) {
        this.addressDao = addressDao;
        this.cityDao = cityDao;
        this.streetDao = streetDao;
    }

    static AddressServiceImpl getInstance(AddressDao addressDao, CityDao cityDao, StreetDao streetDao) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new AddressServiceImpl(addressDao, cityDao, streetDao);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Transactional
    public Address get(Long id) {
        Address address = addressDao.read(id).orElse(null);
        final Long cityId = addressDao.receiveCityId(address);
        final Long streetId = addressDao.receiveStreetId(address);
        final City city = cityDao.read(cityId).orElse(null);
        final Street street = streetDao.read(streetId).orElse(null);
        return Objects.requireNonNull(address).withCity(city).withStreet(street);
    }

    @Override
    @Transactional
    public List<Address> findAll() {
        return addressDao.read().stream()
                .map(address -> this.get(address.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Address add(Address address) {
        try {
            final Address addedAddress = addressDao.create(address);
            return get(addedAddress.getId());
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding address to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
        }
        return null;
    }

    @Override
    @Transactional
    public Address update(Address address) {
        try {
            Address updateAddress = addressDao
                    .update(address
                            .withCity(address.getCity())
                            .withStreet(address.getStreet())
                            .withHouseNumber(address.getHouseNumber())
                            .withFlatNumber(address.getFlatNumber())
                    );
            return get(updateAddress.getId());
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update address information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such address in the database", e);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return addressDao.delete(id);
    }
}
