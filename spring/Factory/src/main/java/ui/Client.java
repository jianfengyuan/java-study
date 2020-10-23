package ui;

import factory.BeanFactory;
import service.IAccountService;
import service.impl.AccountServiceImpl;

public class Client {
    public static void main(String[] args) {
        IAccountService as = null;
        as = (IAccountService) BeanFactory.getBean("accountService");
        as.saveAccount();
    }
}
