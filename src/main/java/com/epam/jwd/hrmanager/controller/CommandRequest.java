package com.epam.jwd.hrmanager.controller;

import java.util.Optional;

public interface CommandRequest {

    void addAttributeToJsp(String name, Object value);

    String getParameter(String name);

    boolean sessionExist();

    boolean addToSession(String name, Object value);

    Optional<Object> retrieveFromSession(String name);

    void createSession();

    void clearSession();
}
