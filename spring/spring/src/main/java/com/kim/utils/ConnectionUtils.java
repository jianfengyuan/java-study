package com.kim.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component("connectionUtils")
public class ConnectionUtils {

    @Autowired
    private DataSource dataSource;
    private ThreadLocal<Connection> tl = new ThreadLocal<>();

    /**
     * 獲取當前線程上的連接**/
    public Connection getThreadConnection() {
        Connection connection = tl.get();
        try {
            if (connection == null) {
                connection = dataSource.getConnection();
            }
            tl.set(connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public void removeConnection() {
        // 當Connection還回連接池後, 該線程要與 Connection 解綁
        tl.remove();
    }
}
