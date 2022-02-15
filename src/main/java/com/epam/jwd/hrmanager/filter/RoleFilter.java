package com.epam.jwd.hrmanager.filter;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.command.CommandRegistry;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class RoleFilter implements Filter {

    private static final String SESSION_ACCOUNT_PROPERTY = "session.account";

    private static final String COMMAND_FORBIDDEN_ERROR_PROPERTY = "command/forbidden_error";

    private static final String COMMAND_PARAM = "command";

    private static final Logger LOGGER = LogManager.getLogger(RoleFilter.class);
    private static final PropertyContext propertyContext = PropertyContext.getInstance();

    private final Map<Role, Set<Command>> commandsByRoles;

    public RoleFilter() {
        commandsByRoles = new EnumMap<>(Role.class);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        Arrays.stream(CommandRegistry.values())
                .forEach(command -> command.getAllowedRoles().forEach(role -> {
                    final Set<Command> commands = commandsByRoles.computeIfAbsent(role, s -> new HashSet<>());
                    commands.add(command.getCommand());
                }));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String commandName = request.getParameter(COMMAND_PARAM);
        LOGGER.trace("Checking permission for command. Command: {}", commandName);
        if (currentUserHasPermissionForCommand(commandName, (HttpServletRequest) request)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect(propertyContext.get(COMMAND_FORBIDDEN_ERROR_PROPERTY));
        }
    }

    private boolean currentUserHasPermissionForCommand(String commandName, HttpServletRequest request) {
        final Role userRole = retrieveCurrentUserRole(request);
        final Command command = CommandRegistry.of(commandName);
        final Set<Command> allowedCommands = commandsByRoles.get(userRole);
        return allowedCommands.contains(command);
    }

    private Role retrieveCurrentUserRole(HttpServletRequest request) {
        return Optional.ofNullable(request.getSession(false))
                .map(s -> (Account) s.getAttribute(propertyContext.get(SESSION_ACCOUNT_PROPERTY)))
                .map(Account::getRole)
                .orElse(Role.UNAUTHORIZED);
    }
}
