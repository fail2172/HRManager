package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.*;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.EntityService;
import com.epam.jwd.hrmanager.secvice.ServiceFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ServiceFactoryImpl implements ServiceFactory {

    private static final String SERVICE_NOT_FOUND = "Could not create service for %s class";
    private final Map<Class<?>, EntityService<?>> serviceByEntity = new ConcurrentHashMap<>();
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private ServiceFactoryImpl() {

    }

    public static ServiceFactoryImpl getInstance(){
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
                    return UserService.getInstance(userDao);
                case "Address":
                    AddressDao addressDao = (AddressDao) daoFactory.daoFor(Address.class);
                    EntityDao<City> cityDao = daoFactory.daoFor(City.class);
                    EntityDao<Street> streetDao = daoFactory.daoFor(Street.class);
                    return AddressService.getInstance(addressDao, cityDao, streetDao);
                case "Vacancy":
                    VacancyDao vacancyDao = (VacancyDao) daoFactory.daoFor(Vacancy.class);
                    EntityDao<Employer> employerDao = daoFactory.daoFor(Employer.class);
                    cityDao = daoFactory.daoFor(City.class);
                    return VacancyService.getInstance(vacancyDao, employerDao, cityDao);
                case "Interview":
                    InterviewDao interviewDao = (InterviewDao) daoFactory.daoFor(Interview.class);
                    EntityService<Address> addressService = serviceFor(Address.class);
                    EntityService<User> userService = serviceFor(User.class);
                    EntityService<Vacancy> vacancyService = serviceFor(Vacancy.class);
                    return InterviewService.getInstance(interviewDao, addressService, userService, vacancyService);
                case "Account":
                    AccountDao accountDao = (AccountDao) daoFactory.daoFor(Account.class);
                    userDao = daoFactory.daoFor(User.class);
                    return AccountService.getInstance(accountDao, userDao);
                default:
                    throw new IllegalStateException(String.format(SERVICE_NOT_FOUND, className));
            }
        };
    }

    private static class Holder {
        public static ServiceFactoryImpl INSTANCE = new ServiceFactoryImpl();
    }
}
