package com.epam.jwd.hrmanager.controller;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.command.CommandResponse;
import com.epam.jwd.hrmanager.db.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 1151124785494302770L;

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    @Override
    public void init() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String commandName = req.getParameter("command");
        final Command command = Command.of(commandName);
        final CommandResponse commandResponse = command.execute(req::setAttribute);
        proceedWithResponse(req, resp, commandResponse);
    }

    private void proceedWithResponse(HttpServletRequest req, HttpServletResponse resp,
                                     CommandResponse commandResponse) {
        try {
            forwardOrRedirectToResponseLocation(req, resp, commandResponse);
        } catch (ServletException e) {
            LOGGER.error("Servlet exception occurred", e);
        } catch (IOException e) {
            LOGGER.error("IO exception occurred", e);
        }
    }

    private void forwardOrRedirectToResponseLocation(HttpServletRequest req, HttpServletResponse resp,
                                                     CommandResponse commandResponse) throws IOException, ServletException {
        if (commandResponse.isRedirect()) {
            resp.sendRedirect(commandResponse.getPath());
        } else {
            final String desiredPath = commandResponse.getPath();
            final RequestDispatcher dispatcher = req.getRequestDispatcher(desiredPath);
            dispatcher.forward(req, resp);
        }
    }
}
