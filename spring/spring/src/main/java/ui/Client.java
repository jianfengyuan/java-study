package ui;

import dao.impl.AccountDaoImpl;
import factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.IAccountService;
import service.impl.AccountServiceImpl;

public class Client {
    public static void main(String[] args) {
        ApplicationContext ac = null;
        ac = new ClassPathXmlApplicationContext("bean.xml");
        IAccountService as = null;
//        as = (IAccountService) BeanFactory.getBean("accountService");
        as = (IAccountService) ac.getBean("accountService");
        as.saveAccount();
    }
}
