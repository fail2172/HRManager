package com.epam.jwd.hrmanager.db.impl;

import com.epam.jwd.hrmanager.db.ResultSetContext;
import com.epam.jwd.hrmanager.model.Entity;

import java.util.List;

public class MultipleContext<T extends Entity> implements ResultSetContext<T> {

    private final List<T> context;

    public MultipleContext(List<T> context) {
        this.context = context;
    }

    public List<T> getContext() {
        return context;
    }
}
