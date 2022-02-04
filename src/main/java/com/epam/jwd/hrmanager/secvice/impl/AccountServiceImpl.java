package com.epam.jwd.hrmanager.secvice.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.epam.jwd.hrmanager.dao.AccountDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.db.TransactionManager;
import com.epam.jwd.hrmanager.exeption.EntityUpdateException;
import com.epam.jwd.hrmanager.exeption.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {

    private static final byte[] DUMMY_PASSWORD = "password".getBytes(StandardCharsets.UTF_8);
    private static final TransactionManager transactionManager = TransactionManager.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(AddressService.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static AccountServiceImpl instance;

    private final AccountDao accountDao;
    private final EntityDao<User> userDao;
    private final BCrypt.Hasher hasher;
    private final BCrypt.Verifyer verifier;

    private AccountServiceImpl(AccountDao accountDao, EntityDao<User> userDao,
                               BCrypt.Hasher hasher, BCrypt.Verifyer verifier) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.hasher = hasher;
        this.verifier = verifier;
    }

    static AccountServiceImpl getInstance(AccountDao accountDao, EntityDao<User> userDao,
                                          BCrypt.Hasher hasher, BCrypt.Verifyer verifier) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new AccountServiceImpl(accountDao, userDao, hasher, verifier);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public Account get(Long id) {
        transactionManager.initTransaction();
        Account account = accountDao.read(id).orElse(null);
        final Long userId = accountDao.receiveUserId(account);
        User user = userDao.read(userId).orElse(null);
        transactionManager.commitTransaction();
        return Objects.requireNonNull(account).withUser(user);
    }

    @Override
    public List<Account> findAll() {
        try {
            transactionManager.initTransaction();
            return accountDao.read().stream()
                    .map(account -> get(account.getId()))
                    .collect(Collectors.toList());
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public Account add(Account account) {
        try {
            transactionManager.initTransaction();
            final char[] rowPassword = account.getPassword().toCharArray();
            final String hashedPassword = hasher.hashToString(BCrypt.MIN_COST, rowPassword);
            final Account addedAccount = accountDao.create(account
                    .withUser(userDao.create(account.getUser()))
                    .withPassword(hashedPassword));
            return get(addedAccount.getId());
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding address to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted", e);
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public Account update(Account account) {
        try {
            transactionManager.initTransaction();
            User updateUser = userDao.create(account.getUser());
            Account updatedAccount = accountDao.update(account
                    .withLogin(account.getLogin())
                    .withEmail(account.getEmail())
                    .withPassword(account.getPassword())
                    .withUser(updateUser)
                    .withRole(account.getRole()));
            return get(updatedAccount.getId());
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update account information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such account in the database", e);
        } finally {
            transactionManager.commitTransaction();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        try {
            transactionManager.initTransaction();
            return accountDao.delete(id);
        } finally {
            transactionManager.commitTransaction();
        }
    }

    @Override
    public Optional<Account> authenticate(String login, String email, String password) {
        if (password == null || (login == null && email == null)) {
            return Optional.empty();
        }
        final Optional<Account> readAccount = tryReceiveAccount(login, email);
        final byte[] enteredPassword = password.getBytes(StandardCharsets.UTF_8);
        if (readAccount.isPresent()) {
            final byte[] hashedPassword = readAccount.get()
                    .getPassword()
                    .getBytes(StandardCharsets.UTF_8);
            return verifier.verify(enteredPassword, hashedPassword).verified
                    ? readAccount
                    : Optional.empty();
        } else {
            protectFromTimingAttack(password.getBytes(StandardCharsets.UTF_8));
            return Optional.empty();
        }
    }

    private Optional<Account> tryReceiveAccount(String login, String email) {
        Optional<Account> account = accountDao.receiveAccountByLogin(login);
        return account.isPresent() ? account : accountDao.receiveAccountByEmail(email);
    }

    private void protectFromTimingAttack(byte[] enterPassword) {
        verifier.verify(enterPassword, DUMMY_PASSWORD);
    }
}
