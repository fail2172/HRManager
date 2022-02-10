package com.epam.jwd.hrmanager.exception;

public class IdExtractionFailedException extends Exception{

    private static final long serialVersionUID = 6431311641516064287L;

    public IdExtractionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
