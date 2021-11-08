package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Entity;

import java.util.List;
import java.util.Optional;

public interface EntityDao<T extends Entity> {

    T create();

    Optional<T> read(Long id);

    List<T> read();

    T update(T t);

    boolean delete(Long id);

}
