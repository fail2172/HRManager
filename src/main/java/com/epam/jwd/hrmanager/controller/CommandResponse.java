package com.epam.jwd.hrmanager.controller;

public interface CommandResponse {

    boolean isRedirect();

    String getPath();

}
