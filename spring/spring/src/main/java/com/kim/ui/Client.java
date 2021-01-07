package com.kim.ui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.kim.service.IAccountService;

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
