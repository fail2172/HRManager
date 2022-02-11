package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Employer;

import java.util.Optional;

public interface EmployerDao extends EntityDao<Employer> {

    Optional<Employer> receiveByName(String name);

}
