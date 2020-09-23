package mybatis.sqlsession.defaults;

import mybatis.cfg.Configuration;
import mybatis.sqlsession.SqlSession;
import mybatis.sqlsession.SqlSessionFactory;

/**
 * @program: mybatisDesign
 * @description:SqlSessionFactory实现类
 * @author: Kim_yuan
 * @create: 2020-09-20 18:31
 **/

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration cfg;
    public DefaultSqlSessionFactory(Configuration configuration) {
        this.cfg = configuration;
    }

    /**
     * 用于创建一个新的操作数据库对象*/


    public SqlSession openSession() {

        return new DefalutSqlSession(cfg);
    }
}
