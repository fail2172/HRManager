package com.epam.jwd.hrmanager.controller.impl;

import com.epam.jwd.hrmanager.controller.CommandRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class WrappingCommandRequest implements CommandRequest {

    private final HttpServletRequest request;

    public WrappingCommandRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void addAttributeToJsp(String name, Object value) {
        request.setAttribute(name, value);
    }

    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    @Override
    public boolean sessionExist() {
        return request.getSession(false) != null;
    }

    @Override
    public boolean addToSession(String name, Object value) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.setAttribute(name, value);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Object> retrieveFromSession(String name) {
        return Optional.ofNullable(request.getSession(false))
                .map(session -> session.getAttribute(name));
    }

    @Override
    public void removeFromSession(String name) {
        if(request.getSession(false) != null){
            request.getSession(false).removeAttribute(name);
        }
    }

    @Override
    public void createSession() {
        this.clearSession();
        request.getSession(true);
    }

    @Override
    public void clearSession() {
        if(request.getSession(false) != null){
            request.getSession(false).invalidate();
        }
    }
}
