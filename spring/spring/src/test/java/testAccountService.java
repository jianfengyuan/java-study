import com.kim.config.SpringConfiguration;
import com.kim.dao.IAccountDao;
import com.kim.domain.Account;
import com.kim.service.IAccountService;
import com.kim.service.impl.AccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class testAccountService {
    @Autowired
    private IAccountService accountService;
    @Test
    public void testFindAll() {
//        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
//        IAccountService accountService = ac.getBean("accountService", AccountServiceImpl.class);
        List<Account> accounts = accountService.findAll();
        for (Account account :
                accounts) {
            System.out.println(account);
        }
    }

    @Test
    public void testFindById() {
//        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
//        IAccountService accountServiceDao = ac.getBean("accountService", AccountServiceImpl.class);
        Account account = accountService.findAccountByID(1);
        System.out.println(account);

    }
}
