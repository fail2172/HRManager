package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.model.Account;

import java.util.Optional;

public interface AccountService extends EntityService<Account> {

    Optional<Account> authenticate(String email, String password);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByLogin(String login);
}
