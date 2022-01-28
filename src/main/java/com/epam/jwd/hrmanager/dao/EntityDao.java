package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.exeption.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.Entity;

import java.util.List;
import java.util.Optional;

public interface EntityDao<T extends Entity> {

    T create(T entity) throws EntityUpdateException, InterruptedException;

    Optional<T> read(Long id);

    List<T> read();

    T update(T t) throws InterruptedException, EntityUpdateException, NotFoundEntityException;

    boolean delete(Long id);

}
