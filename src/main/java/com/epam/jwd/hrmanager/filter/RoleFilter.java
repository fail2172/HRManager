package com.epam.jwd.hrmanager.filter;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.command.CommandRegistry;
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

    private static final Logger LOGGER = LogManager.getLogger(RoleFilter.class);
    public static final String COMMAND_PARAM_NAME = "command";
    private static final String ERROR_PAGE_URL = "/controller?command=show_error";
    public static final String ACCOUNT_SESSION_ATTRIBUTE_NAME = "account";

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
        final String commandName = request.getParameter(COMMAND_PARAM_NAME);
        LOGGER.trace("Checking permission for command. Command: {}", commandName);
        if (currentUserHasPermissionForCommand(commandName, (HttpServletRequest) request)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect(ERROR_PAGE_URL);
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
                .map(s -> (Account) s.getAttribute(ACCOUNT_SESSION_ATTRIBUTE_NAME))
                .map(Account::getRole)
                .orElse(Role.UNAUTHORIZED);
    }
}
