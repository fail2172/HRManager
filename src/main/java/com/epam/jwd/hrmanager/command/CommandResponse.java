package com.epam.jwd.hrmanager.command;

public interface CommandResponse {

    boolean isRedirect();

    String getPath();

}
