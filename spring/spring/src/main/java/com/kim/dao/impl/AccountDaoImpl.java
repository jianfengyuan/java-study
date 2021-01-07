package com.kim.dao.impl;

import com.kim.dao.IAccountDao;
import com.kim.domain.Account;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository("accountDao")
public class AccountDaoImpl implements IAccountDao {
    @Autowired
    private QueryRunner runner;

    public void save() {

        System.out.println("保存了賬戶");
    }



    @Override
    public List<Account> findAll() {
        try {
            return runner.query("select * from account", new BeanListHandler<>(Account.class));
        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    @Override
    public Account findAccountByID(Integer AccountID) {
        try {
            return runner.query("select * from account where id = ?", new BeanHandler<>(Account.class),AccountID);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Account findAccountByName(String name) {
        try {
            List<Account> accounts = runner.query("select * from account where name = ?",
                    new BeanListHandler<>(Account.class),name);
            if (accounts == null || accounts.size() == 0) {
                return null;
            }
            if (accounts.size() > 1) {
                throw new RuntimeException();
            }
            return accounts.get(0);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
