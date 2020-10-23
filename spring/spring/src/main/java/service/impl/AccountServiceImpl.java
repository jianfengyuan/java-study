package service.impl;

import dao.IAccountDao;
import dao.impl.AccountDaoImpl;
import factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.IAccountService;

public class AccountServiceImpl implements IAccountService {
    private IAccountDao accountDao;
    public void saveAccount() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
//        accountDao = (IAccountDao) BeanFactory.getBean("accountDao");
        accountDao = ac.getBean("accountDao", AccountDaoImpl.class);
        accountDao.save();
    }
}
