package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.AccountDao;
import com.epam.jwd.hrmanager.dao.DaoFactory;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.EntityService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AccountService implements EntityService<Account> {

    private final AccountDao accountDao;
    private final EntityDao<User> userDao;

    private AccountService(AccountDao accountDao, EntityDao<User> userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    static AccountService getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    public Account get(Long id) {
        Account account = accountDao.read(id).orElse(null);
        final Long userId = accountDao.receiveUserId(account).orElse(null);
        User user = userDao.read(userId).orElse(null);
        return Objects.requireNonNull(account).withUser(user);
    }

    @Override
    public List<Account> findAll() {
        return accountDao.read().stream()
                .map(account -> get(account.getId()))
                .collect(Collectors.toList());
    }

    private static class Holder {
        private static final AccountService INSTANCE = new AccountService(
                (AccountDao) DaoFactory.getInstance().daoFor(Account.class),
                DaoFactory.getInstance().daoFor(User.class)
        );
    }
}
