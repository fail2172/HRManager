package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.AccountStatus;
import com.epam.jwd.hrmanager.secvice.AccountService;

import java.util.concurrent.locks.ReentrantLock;

public class BanCommand implements Command {

    private static final String INDEX_PAGE = "page.index";

    private static final String ACCOUNT_SESSION_ATTRIBUTE = "sessionAccount";
    private static final ReentrantLock lock = new ReentrantLock();
    private static BanCommand instance;

    private final RequestFactory requestFactory;
    private final AccountService accountService;
    private final PropertyContext propertyContext;

    private BanCommand(RequestFactory requestFactory, AccountService accountService,
                                     PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.accountService = accountService;
        this.propertyContext = propertyContext;
    }

    static BanCommand getInstance(RequestFactory requestFactory, AccountService accountService,
                                                PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new BanCommand(requestFactory, accountService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Account account = accountService.get(Long.parseLong(request.getParameter("accountId")));
        if (account.getStatus().equals(AccountStatus.UNBANNED)) {
            accountService.update(account.withStatus(AccountStatus.BANNED));
        }
        return requestFactory.createRedirectResponse("/controller?command=usersPage");
    }
}
