package com.epam.jwd.hrmanager.exeption;


public class CouldNotInitialiseConnectionService extends Error {

    private static final long serialVersionUID = 7944332529441314649L;

    public CouldNotInitialiseConnectionService(String message, Throwable cause) {
        super(message, cause);
    }
}
