package com.epam.jwd.hrmanager.exception;

public class NoSuchEntityServiceException extends Exception {

    private static final long serialVersionUID = -8422885652921725775L;

    public NoSuchEntityServiceException(String message) {
        super(message);
    }
}
