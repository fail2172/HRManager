package com.epam.jwd.hrmanager.secvice.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.epam.jwd.hrmanager.dao.AccountDao;
import com.epam.jwd.hrmanager.dao.EntityDao;
import com.epam.jwd.hrmanager.exception.EntityUpdateException;
import com.epam.jwd.hrmanager.exception.NotFoundEntityException;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.AccountStatus;
import com.epam.jwd.hrmanager.model.User;
import com.epam.jwd.hrmanager.secvice.AccountService;
import com.epam.jwd.hrmanager.transaction.Transactional;
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
    private static final Logger LOGGER = LogManager.getLogger(AddressServiceImpl.class);
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
    @Transactional
    public Account get(Long id) {
        Account account = accountDao.read(id).orElse(null);
        final Long userId = accountDao.receiveUserId(account);
        User user = userDao.read(userId).orElse(null);
        return Objects.requireNonNull(account).withUser(user);
    }

    @Override
    @Transactional
    public List<Account> findAll() {
        return accountDao.read().stream()
                .map(account -> get(account.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Account add(Account account) {
        try {
            final char[] rowPassword = account.getPassword().toCharArray();
            final String hashedPassword = hasher.hashToString(BCrypt.MIN_COST, rowPassword);
            final Account addedAccount = accountDao.create(account.withPassword(hashedPassword));
            return get(addedAccount.getId());
        } catch (EntityUpdateException e) {
            LOGGER.error("Error adding address to database", e);
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted", e);
        }
        return null;
    }

    @Override
    @Transactional
    public Account update(Account account) {
        try {
            Account updatedAccount = accountDao.update(account
                    .withLogin(account.getLogin())
                    .withEmail(account.getEmail())
                    .withPassword(account.getPassword())
                    .withUser(account.getUser())
                    .withRole(account.getRole()));
            return get(updatedAccount.getId());
        } catch (InterruptedException e) {
            LOGGER.warn("take connection interrupted");
            Thread.currentThread().interrupt();
        } catch (EntityUpdateException e) {
            LOGGER.error("Failed to update account information", e);
        } catch (NotFoundEntityException e) {
            LOGGER.error("there is no such account in the database", e);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return accountDao.delete(id);
    }

    @Override
    @Transactional
    public Optional<Account> authenticate(String email, String password) {
        if (password == null || email == null) {
            return Optional.empty();
        }
        final Optional<Account> readAccount = findByEmail(email);
        final byte[] enteredPassword = password.getBytes(StandardCharsets.UTF_8);
        if (readAccount.isPresent()) {
            final byte[] hashedPassword = readAccount.get()
                    .getPassword()
                    .getBytes(StandardCharsets.UTF_8);
            return verifier.verify(enteredPassword, hashedPassword).verified
                    && readAccount.get().getStatus().equals(AccountStatus.UNBANNED)
                    ? Optional.of(get(readAccount.get().getId()))
                    : Optional.empty();
        } else {
            protectFromTimingAttack(password.getBytes(StandardCharsets.UTF_8));
            return Optional.empty();
        }
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountDao.receiveAccountByEmail(email);
    }

    @Override
    public Optional<Account> findByLogin(String login) {
        return accountDao.receiveAccountByLogin(login);
    }

    private void protectFromTimingAttack(byte[] enterPassword) {
        verifier.verify(enterPassword, DUMMY_PASSWORD);
    }
}
