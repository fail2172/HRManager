package com.epam.jwd.hrmanager.exeption;


public class CouldNotInitialisationConnectionService extends Error {

    private static final long serialVersionUID = 7944332529441314649L;

    public CouldNotInitialisationConnectionService(String message, Throwable cause) {
        super(message, cause);
    }
}
