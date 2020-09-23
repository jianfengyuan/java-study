package mybatis.sqlsession;

import mybatis.cfg.Configuration;
import mybatis.sqlsession.defaults.DefaultSqlSessionFactory;
import mybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 * @program: mybatisDesign
 * @description: 创建SqlSessionFactory对象
 * @author: Kim_yuan
 * @create: 2020-09-20 17:21
 **/

public class SqlSessionFactoryBuilder {
    /**
     * 根据参数的字节输入流构建一个SqlSessionFactory对象
     * */
    public SqlSessionFactory build(InputStream config) {
        Configuration cfg = XMLConfigBuilder.loadConfiguration(config);

        return new DefaultSqlSessionFactory(cfg);
    }
}
