package com.kim.service.impl;

import com.kim.config.SpringConfiguration;
import com.kim.dao.IAccountDao;
import com.kim.dao.impl.AccountDaoImpl;
import com.kim.domain.Account;
import com.kim.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import com.kim.service.IAccountService;

import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private IAccountDao accountDao;
    public void saveAccount() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
//        accountDao = (IAccountDao) BeanFactory.getBean("accountDao");
//        accountDao = ac.getBean("accountDao", AccountDaoImpl.class);
        accountDao.save();
    }

    @Override
    public List<Account> findAll() {
        return accountDao.findAll();
    }

    @Override
    public Account findAccountByID(Integer accountID) {
        return accountDao.findAccountByID(accountID);
    }

    @Override
    public void transfer(String sourceName, String targetName, float money) {
        Account source = accountDao.findAccountByName(sourceName);
        Account target = accountDao.findAccountByName(targetName);
        source.setMoney(source.getMoney() - money);
        target.setMoney(target.getMoney() + money);
    }
}
