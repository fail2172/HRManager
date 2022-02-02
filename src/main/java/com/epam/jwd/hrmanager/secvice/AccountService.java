package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.model.Account;

import java.util.Optional;

public interface AccountService extends EntityService<Account> {

    Optional<Account> authenticate(String login, String password);

}
