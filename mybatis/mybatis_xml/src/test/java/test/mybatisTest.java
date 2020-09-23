package test;

import dao.IUserDao;
import domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @program: my_batis
 * @description:
 * @author: Kim_yuan
 * @create: 2020-09-20 14:27
 **/

public class mybatisTest {
    @Test
    public static void testfindAll(String[] args) throws Exception {
        // 1. 读取配置文件
        InputStream in = Resources.getResourceAsStream("myBatisConf.xml");
        // 2. 创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        // 3. 使用工厂产生SqlSession对象
        SqlSession session = factory.openSession();
        // 4. 使用SqlSession创建Dao接口的代理对象
        IUserDao userDao = session.getMapper(IUserDao.class);
        // 5. 使用代理对象执行方法
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println(user);
        }
        // 6. 释放资源
        session.close();
        in.close();
    }
}
