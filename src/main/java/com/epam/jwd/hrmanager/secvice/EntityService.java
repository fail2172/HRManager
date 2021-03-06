package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.model.Entity;

import java.util.List;

public interface EntityService<T extends Entity> {

    T get(Long id);

    List<T> findAll();

    T add(T t);

    T update(T t);

    boolean delete(Long id);

}
