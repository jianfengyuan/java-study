package service.impl;

import dao.IAccountDao;
import dao.impl.AccountDaoImpl;
import factory.BeanFactory;
import service.IAccountService;

public class AccountServiceImpl implements IAccountService {
    private IAccountDao accountDao;
    public void saveAccount() {
        accountDao = (IAccountDao) BeanFactory.getBean("accountDao");
        accountDao.save();
    }
}
