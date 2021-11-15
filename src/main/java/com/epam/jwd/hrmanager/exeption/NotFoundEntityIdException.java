package com.epam.jwd.hrmanager.exeption;

public class NotFoundEntityIdException extends Exception{

    private static final long serialVersionUID = -5188818482182519956L;

    public NotFoundEntityIdException(String message) {
        super(message);
    }
}
