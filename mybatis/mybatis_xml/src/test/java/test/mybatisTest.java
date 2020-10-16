package test;

import dao.IAccountDao;
import dao.IUserDao;
import domain.Account;
import domain.AccountUser;
import domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.List;

/**
 * @program: my_batis
 * @description:
 * @author: Kim_yuan
 * @create: 2020-09-20 14:27
 **/

public class mybatisTest {
    private InputStream in;
    private SqlSession session;
    private IUserDao userDao;
    private IAccountDao accountDao;

    @Before
    public void init() throws Exception{
        // 1. 读取配置文件
        in = Resources.getResourceAsStream("myBatisConf.xml");
        // 2. 创建SqlSessionFactory工厂
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        // 3. 使用工厂产生SqlSession对象
        session = factory.openSession();
        userDao = session.getMapper(IUserDao.class);
        accountDao = session.getMapper(IAccountDao.class);
    }

    @After
    public void destroy() throws IOException {
        session.close();
        in.close();

    }

    @Test
    public void testFindAll() throws Exception {

        // 5. 使用代理对象执行方法
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println(user);
        }
        // 6. 释放资源
        session.close();
        in.close();
    }

    @Test
    public void testFindAllAccountUser() {
        List<Account> accountUsers = accountDao.findAllAccountUser();
        for (Account au:  accountUsers
             ) {
            System.out.println(au);
            System.out.println(au.getUser());
        }
    }

    @Test
    public void testUserFindAll() {
        List<User> users = userDao.findUserAccount();
        for (User user :
                users) {
            System.out.println(user);
        }
    }

    @Test
    public void testFindUserRole() {
        List<User> users = userDao.findUserRole();
        for (User user : users) {
            System.out.println(user);
        }
    }
}
