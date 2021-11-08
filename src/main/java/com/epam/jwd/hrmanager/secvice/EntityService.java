package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.model.Entity;

import java.util.List;

public interface EntityService<T extends Entity> {

    List<T> findAll();

}
