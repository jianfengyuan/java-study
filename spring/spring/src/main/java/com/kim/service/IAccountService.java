package com.kim.service;

import com.kim.domain.Account;

import java.util.List;

public interface IAccountService {
    void saveAccount();

    List<Account> findAll();

    Account findAccountByID(Integer accountID);

    void transfer(String sourceName, String targetName, float money);
}
