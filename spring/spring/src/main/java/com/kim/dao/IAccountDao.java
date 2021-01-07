package com.kim.dao;

import com.kim.domain.Account;

import java.util.List;

public interface IAccountDao {
    void save();

    List<Account> findAll();

    Account findAccountByID(Integer Id);

    Account findAccountByName(String name);
}
