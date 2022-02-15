package com.epam.jwd.hrmanager.command.impl.action;

import com.epam.jwd.hrmanager.command.Authorized;
import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.AccountStatus;
import com.epam.jwd.hrmanager.secvice.AccountService;

import java.util.concurrent.locks.ReentrantLock;

public class UnBanCommand implements Command {

    public static final String COMMAND_USERS_PAGE_PROPERTY = "command/users_page";

    public static final String ACCOUNT_ID_PARAM = "accountId";

    private static final ReentrantLock lock = new ReentrantLock();
    private static UnBanCommand instance;

    private final RequestFactory requestFactory;
    private final AccountService accountService;
    private final PropertyContext propertyContext;

    private UnBanCommand(RequestFactory requestFactory, AccountService accountService,
                         PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.accountService = accountService;
        this.propertyContext = propertyContext;
    }

    public static UnBanCommand getInstance(RequestFactory requestFactory, AccountService accountService,
                                           PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new UnBanCommand(requestFactory, accountService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    @Authorized
    public CommandResponse execute(CommandRequest request) {
        final Account account = accountService.get(Long.parseLong(request.getParameter(ACCOUNT_ID_PARAM)));
        if (account.getStatus().equals(AccountStatus.BANNED)) {
            accountService.update(account.withStatus(AccountStatus.UNBANNED));
        }
        return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_USERS_PAGE_PROPERTY));
    }
}
