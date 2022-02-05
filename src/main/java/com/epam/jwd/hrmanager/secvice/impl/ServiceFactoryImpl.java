package com.epam.jwd.hrmanager.secvice.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.epam.jwd.hrmanager.dao.*;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.EntityService;
import com.epam.jwd.hrmanager.secvice.ServiceFactory;
import com.epam.jwd.hrmanager.transaction.ProxyWithTransactions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ServiceFactoryImpl implements ServiceFactory {

    private static final String SERVICE_NOT_FOUND = "Could not create service for %s class";
    private final Map<Class<?>, EntityService<?>> serviceByEntity = new HashMap<>();
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private ServiceFactoryImpl() {

    }

    public static ServiceFactoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> EntityService<T> serviceFor(Class<T> modelClass) {
        return (EntityService<T>) serviceByEntity
                .computeIfAbsent(modelClass, createServiceForEntity());
    }

    private Function<Class<?>, EntityService<?>> createServiceForEntity() {
        return clazz -> {
            final String className = clazz.getSimpleName();
            switch (className) {
                case "User":
                    EntityDao<User> userDao = daoFactory.daoFor(User.class);
                    return withTransactions(UserServiceImpl.getInstance(userDao));
                case "Address":
                    AddressDao addressDao = (AddressDao) daoFactory.daoFor(Address.class);
                    EntityDao<City> cityDao = daoFactory.daoFor(City.class);
                    EntityDao<Street> streetDao = daoFactory.daoFor(Street.class);
                    return withTransactions(AddressServiceImpl.getInstance(addressDao, cityDao, streetDao));
                case "Vacancy":
                    VacancyDao vacancyDao = (VacancyDao) daoFactory.daoFor(Vacancy.class);
                    EntityDao<Employer> employerDao = daoFactory.daoFor(Employer.class);
                    cityDao = daoFactory.daoFor(City.class);
                    return withTransactions(VacancyServiceImpl.getInstance(vacancyDao, employerDao, cityDao));
                case "Interview":
                    InterviewDao interviewDao = (InterviewDao) daoFactory.daoFor(Interview.class);
                    EntityService<Address> addressService = serviceFor(Address.class);
                    EntityService<User> userService = serviceFor(User.class);
                    EntityService<Vacancy> vacancyService = serviceFor(Vacancy.class);
                    return withTransactions(
                            InterviewServiceImpl.getInstance(interviewDao, addressService, userService, vacancyService)
                    );
                case "Account":
                    AccountDao accountDao = (AccountDao) daoFactory.daoFor(Account.class);
                    userDao = daoFactory.daoFor(User.class);
                    return withTransactions(
                            AccountServiceImpl.getInstance(accountDao, userDao, BCrypt.withDefaults(), BCrypt.verifyer())
                    );
                case "City":
                    cityDao = daoFactory.daoFor(City.class);
                    return withTransactions(CityServiceImpl.getInstance(cityDao));
                case "Street":
                    streetDao = daoFactory.daoFor(Street.class);
                    return withTransactions(StreetServiceImpl.getInstance(streetDao));
                case "Employer":
                    employerDao = daoFactory.daoFor(Employer.class);
                    return withTransactions(EmployerServiceImpl.getInstance(employerDao));
                default:
                    throw new IllegalStateException(String.format(SERVICE_NOT_FOUND, className));
            }
        };
    }

    private <T extends Entity> EntityService<T> withTransactions(EntityService<T> service) {
        return ProxyWithTransactions.of(service);
    }

    private static class Holder {
        public static ServiceFactoryImpl INSTANCE = new ServiceFactoryImpl();
    }
}