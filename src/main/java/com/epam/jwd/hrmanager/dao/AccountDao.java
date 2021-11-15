package com.epam.jwd.hrmanager.dao;

import com.epam.jwd.hrmanager.model.Account;

import java.util.Optional;

public interface AccountDao extends EntityDao<Account>{

    Optional<Long> receiveUserId(Account account);

}
