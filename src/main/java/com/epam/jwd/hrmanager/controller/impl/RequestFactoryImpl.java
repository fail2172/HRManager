package com.epam.jwd.hrmanager.controller.impl;

import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestFactoryImpl implements RequestFactory {

    private final Map<String, CommandResponse> forwardResponseCash = new ConcurrentHashMap<>();
    private final Map<String, CommandResponse> redirectResponseCash = new ConcurrentHashMap<>();

    private RequestFactoryImpl() {

    }

    public static RequestFactoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandRequest createRequest(HttpServletRequest request) {
        return new WrappingCommandRequest(request);
    }

    @Override
    public CommandResponse createForwardResponse(String path) {
        return forwardResponseCash.computeIfAbsent(path, PlainCommandResponse::new);
    }

    @Override
    public CommandResponse createRedirectResponse(String path) {
        return redirectResponseCash.computeIfAbsent(path, p -> new PlainCommandResponse(true, path));
    }

    private static final class Holder {
        private static final RequestFactoryImpl INSTANCE = new RequestFactoryImpl();
    }
}
