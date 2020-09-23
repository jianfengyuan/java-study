package mybatis.sqlsession.defaults;

import mybatis.cfg.Configuration;
import mybatis.sqlsession.SqlSession;
import mybatis.sqlsession.proxy.MapperProxy;
import mybatis.utils.DataSourceUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @program: mybatisDesign
 * @description:
 * @author: Kim_yuan
 * @create: 2020-09-20 18:36
 **/

public class DefalutSqlSession implements SqlSession {
    private Configuration cfg;
    private Connection connection;
    public DefalutSqlSession(Configuration cfg) {
        this.cfg = cfg;
        connection = DataSourceUtil.getConnection(cfg);
    }

    public <T> T getMapper(Class<T> daoInterfaceClass) {
        Proxy.newProxyInstance(daoInterfaceClass.getClassLoader(),
                new Class[]{daoInterfaceClass},
                new MapperProxy(cfg.getMappers(), connection));
        return null;
    }


    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
