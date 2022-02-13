package com.epam.jwd.hrmanager.controller;

import com.epam.jwd.hrmanager.controller.impl.RequestFactoryImpl;

import javax.servlet.http.HttpServletRequest;

public interface RequestFactory {

    CommandRequest createRequest(HttpServletRequest request);

    CommandResponse createForwardResponse(String path);

    CommandResponse createRedirectResponse(String path);

    static RequestFactory getInstance() {
        return RequestFactoryImpl.getInstance();
    }

}
