package com.epam.jwd.hrmanager.exeption;

import java.util.function.Supplier;

public class EntityUpdateException extends Exception {

    public EntityUpdateException(String message) {
        super(message);
    }
}
