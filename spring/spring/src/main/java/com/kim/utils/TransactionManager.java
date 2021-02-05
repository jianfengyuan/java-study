package com.kim.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component("transactionManager")
public class TransactionManager {

    // 获取本地线程上的connection 进行事务管理
    @Autowired
    private ConnectionUtils connectionUtils;

    public void StartTransaction() {
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
    public void commit() {
        try {
            connectionUtils.getThreadConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
    public void rollback() {
        try {
            connectionUtils.getThreadConnection().rollback();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
    public void release() {
        try {
            connectionUtils.getThreadConnection().close(); // 把連接還回連接池中
            connectionUtils.removeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
