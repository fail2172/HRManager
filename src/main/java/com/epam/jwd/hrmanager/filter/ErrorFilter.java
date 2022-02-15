package com.epam.jwd.hrmanager.filter;

import com.epam.jwd.hrmanager.controller.PropertyContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorFilter implements Filter {

    private static final String COMMAND_SERVER_ERROR_PROPERTY = "command/server_error";

    private static final PropertyContext propertyContext = PropertyContext.getInstance();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            ((HttpServletResponse) response).sendRedirect(propertyContext.get(COMMAND_SERVER_ERROR_PROPERTY));
        }
    }
}
