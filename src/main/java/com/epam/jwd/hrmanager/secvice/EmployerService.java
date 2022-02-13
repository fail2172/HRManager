package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.model.Employer;

import java.util.Optional;

public interface EmployerService extends EntityService<Employer> {

    Optional<Employer> receiveByName(String name);

}
