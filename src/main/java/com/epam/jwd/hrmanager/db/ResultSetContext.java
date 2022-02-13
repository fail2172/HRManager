package com.epam.jwd.hrmanager.db;

import com.epam.jwd.hrmanager.db.impl.MultipleContext;
import com.epam.jwd.hrmanager.db.impl.SingleContext;
import com.epam.jwd.hrmanager.model.Entity;

import java.util.List;

public interface ResultSetContext<T extends Entity> {

    static <T extends Entity>ResultSetContext<T> single(T context){
        return new SingleContext<T>(context);
    }

    static <T extends Entity>ResultSetContext<T> multiple(List<T> context){
        return new MultipleContext<T>(context);
    }

}
