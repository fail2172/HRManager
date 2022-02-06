package com.epam.jwd.hrmanager.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorFilter implements Filter {

    private static final String ERROR_PAGE_URL = "/controller?command=show_error";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e){
            ((HttpServletResponse)response).sendRedirect(ERROR_PAGE_URL);
        }
    }
}
