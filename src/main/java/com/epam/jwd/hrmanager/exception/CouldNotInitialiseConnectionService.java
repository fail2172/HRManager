package com.epam.jwd.hrmanager.exception;

public class CouldNotInitialiseConnectionService extends RuntimeException {

    private static final long serialVersionUID = 6838433404928178510L;

    public CouldNotInitialiseConnectionService(String message, Throwable cause) {
        super(message, cause);
    }

}
