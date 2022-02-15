package com.epam.jwd.hrmanager.controller;

import java.util.Optional;

public interface CommandRequest {

    Optional<Object> retrieveFromSession(String name);

    String getParameter(String name);

    String getPath();

    boolean sessionExist();

    boolean addToSession(String name, Object value);

    void addAttributeToJsp(String name, Object value);

    void removeFromSession(String name);

    void createSession();

    void clearSession();
}
