package com.epam.jwd.hrmanager.controller;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.EntityService;
import com.epam.jwd.hrmanager.secvice.ServiceFactory;
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

    private static final long serialVersionUID = 8238816177981432076L;
    private static final Logger LOGGER = LogManager.getLogger(Controller.class);
    public static final String COMMAND_NAME_PARAM = "command";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        LOGGER.trace("caught req and resp in doGet method");
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        LOGGER.trace("caught req and resp in doPost method");
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        final String commandName = req.getParameter(COMMAND_NAME_PARAM);
        LOGGER.info("Command execution: {}", commandName);
        final Command command = Command.of(commandName);
        final CommandRequest commandRequest = requestFactory.createRequest(req);
        final CommandResponse commandResponse = command.execute(commandRequest);
        proceedWithResponse(req, resp, commandResponse);
    }

    private void proceedWithResponse(HttpServletRequest req, HttpServletResponse resp, CommandResponse commandResponse) {
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

    private void main() {
        EntityService<User> userService = ServiceFactory.getInstance().serviceFor(User.class);
        System.out.println(userService.findAll());
    }
}
