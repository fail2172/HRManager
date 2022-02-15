package com.epam.jwd.hrmanager.filter;

import com.epam.jwd.hrmanager.controller.PropertyContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ControllerFilter implements Filter {

    private static final PropertyContext propertyContext = PropertyContext.getInstance();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            final String controller = ((HttpServletRequest) request).getServletPath();
            if (controller.equals("/controller") || controller.equals("/index.jsp")) {
                chain.doFilter(request, response);
            } else {
                ((HttpServletResponse) response).sendRedirect(propertyContext.get("command/not_found_error"));
            }
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
