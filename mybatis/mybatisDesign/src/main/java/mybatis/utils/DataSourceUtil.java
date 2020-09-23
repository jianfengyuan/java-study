package mybatis.utils;

import mybatis.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @program: mybatisDesign
 * @description:
 * @author: Kim_yuan
 * @create: 2020-09-20 18:53
 **/

public class DataSourceUtil {
    public static Connection getConnection(Configuration configuration) {
        try {
            Class.forName(configuration.getDriver());
            return DriverManager.getConnection(configuration.getUrl(),
                    configuration.getUsername(),
                    configuration.getPassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
