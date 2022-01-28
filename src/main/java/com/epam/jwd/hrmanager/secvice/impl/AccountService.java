package com.epam.jwd.hrmanager.secvice.impl;

import com.epam.jwd.hrmanager.dao.AccountDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.EntityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AccountService implements EntityService<Account> {

    private static final TransactionManager transactionManager = TransactionManager.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(AddressService.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static AccountService instance;

    private final AccountDao accountDao;
    private final EntityDao<User> userDao;

    private AccountService(AccountDao accountDao, EntityDao<User> userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    static AccountService getInstance(AccountDao accountDao, EntityDao<User> userDao){
        if(instance == null){
            lock.lock();
            {
                if(instance == null){
                    instance = new AccountService(accountDao, userDao);
                }
            }
            lock.unlock();
        }
        return instance;
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

    @Override
    public Account add(Account account) {
        try {
            final Account addedAccount = accountDao.create(account
                    .withUser(userDao.create(account.getUser())));
            return get(addedAccount.getId());
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding address to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
        }
        return null;
    }

    @Override
    public Account update(Account account) {
        return null;
    }
}
