package com.epam.jwd.hrmanager.exeption;

public class NotFoundEntityException extends Exception{
    private static final long serialVersionUID = -1918121303334184793L;

    public NotFoundEntityException(String message) {
        super(message);
    }
}
