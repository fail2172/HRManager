package com.epam.jwd.hrmanager.exeption;

public class OpenPropertiesError extends Error{
    private static final long serialVersionUID = -6124390427848305790L;

    public OpenPropertiesError(String message, Throwable cause) {
        super(message, cause);
    }
}
