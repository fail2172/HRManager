package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.db.ResultSetContext;
import com.epam.jwd.hrmanager.model.Entity;

import java.util.Optional;

public class SingleContext<T extends Entity> implements ResultSetContext<T> {

    private final T context;

    public SingleContext(T context) {
        this.context = context;
    }

    public Optional<T> getContext() {
        return Optional.of(context);
    }
}
