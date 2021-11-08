package com.epam.jwd.hrmanager.exeption;

public class EntityExtractionFailedException extends Exception {

    private static final long serialVersionUID = 6294246311158592455L;

    public EntityExtractionFailedException(String message) {
        super(message);
    }
}
