package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.AddressDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.exeption.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.Address;
import com.epam.jwd.hrmanager.model.City;
import com.epam.jwd.hrmanager.model.Street;
import com.epam.jwd.hrmanager.secvice.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AddressService implements EntityService<Address> {

    private static final TransactionManager transactionManager = TransactionManager.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(AddressService.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static AddressService instance;

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
        transactionManager.initTransaction();
        Address address = addressDao.read(id).orElse(null);
        final Long cityId = addressDao.receiveCityId(address).orElse(null);
        final Long streetId = addressDao.receiveStreetId(address).orElse(null);
        final City city = cityDao.read(cityId).orElse(null);
        final Street street = streetDao.read(streetId).orElse(null);
        transactionManager.commitTransaction();
        return Objects.requireNonNull(address).withCity(city).withStreet(street);
    }

    @Override
    public List<Address> findAll() {
        try {
            transactionManager.initTransaction();
            return addressDao.read().stream()
                    .map(address -> this.get(address.getId()))
                    .collect(Collectors.toList());
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public Address add(Address address) {
        try {
            transactionManager.initTransaction();
            final Address addedAddress = addressDao.create(address
                    .withCity(cityDao.create(address.getCity()))
                    .withStreet(streetDao.create(address.getStreet())));
            return get(addedAddress.getId());
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding address to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public Address update(Address address) {
        try {
            transactionManager.initTransaction();
            City updateCity = cityDao.create(address.getCity());
            Street updateStreet = streetDao.create(address.getStreet());
            Address updateAddress = addressDao
                    .update(address
                            .withCity(updateCity)
                            .withStreet(updateStreet)
                            .withHouseNumber(address.getHouseNumber())
                            .withFlatNumber(address.getFlatNumber().orElse(null))
                    );
            return get(updateAddress.getId());
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update address information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such address in the database", e);
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        try {
            transactionManager.initTransaction();
            return addressDao.delete(id);
        } finally {
            transactionManager.commitTransaction();
        }
    }
}
