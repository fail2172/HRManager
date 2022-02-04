package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Account;

import java.util.Optional;

public interface AccountDao extends EntityDao<Account>{

    Long receiveUserId(Account account);

    Optional<Account> receiveAccountByLogin(String login);

    Optional<Account> receiveAccountByEmail(String email);

}
