package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.model.Entity;
import com.epam.jwd.hrmanager.secvice.impl.ServiceFactoryImpl;

public interface ServiceFactory {

    <T extends Entity> EntityService<T> serviceFor(Class<T> modelClass);

    static ServiceFactory getInstance() {
        return ServiceFactoryImpl.getInstance();
    }

}
