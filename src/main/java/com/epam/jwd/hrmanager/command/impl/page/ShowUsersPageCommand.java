package com.epam.jwd.hrmanager.command.impl.page;

import com.epam.jwd.hrmanager.command.Authorized;
import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.Role;
import com.epam.jwd.hrmanager.secvice.AccountService;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ShowUsersPageCommand implements Command {

    private static final String USER_PAGE = "page.users";

    private static final String ACCOUNTS_ATTRIBUTE = "accounts";
    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowUsersPageCommand instance;

    private final RequestFactory requestFactory;
    private final AccountService accountService;
    private final PropertyContext propertyContext;

    private ShowUsersPageCommand(RequestFactory requestFactory, AccountService accountService,
                                 PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.accountService = accountService;
        this.propertyContext = propertyContext;
    }

    public static ShowUsersPageCommand getInstance(RequestFactory requestFactory,
                                                   AccountService accountService,
                                                   PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ShowUsersPageCommand(requestFactory, accountService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Authorized
    public CommandResponse execute(CommandRequest request) {
        final List<Account> accounts = accountService.findAll().stream()
                .filter(a -> !a.getRole().equals(Role.ADMINISTRATOR))
                .collect(Collectors.toList());
        request.addAttributeToJsp(ACCOUNTS_ATTRIBUTE, accounts);
        return requestFactory.createForwardResponse(propertyContext.get(USER_PAGE));
    }
}
