package com.risen.phoenix.jdbc.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Aspect
@Component
public class PhoenixAspect {

    @Autowired
    PhoenixTransactionManager phoenixTransactionManager;

    @Around("@annotation(com.risen.phoenix.jdbc.annotations.PhxTransaction)")
    public Object doTransaction(ProceedingJoinPoint point) throws SQLException {
        System.out.println("1.开始测试******************获取链接************");
        Connection connection = phoenixTransactionManager.getConnection();
        connection.setAutoCommit(false);
        System.out.println("2.已获取连接****************执行代码************");
        try {
            point.proceed();
            System.out.println("3.执行完毕****************提交实物************");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("4.异常error****************回滚************");
            connection.rollback();
        }finally {
            phoenixTransactionManager.closeResource();
        }
        System.out.println("5.执行成功***********************************");
        return null;
    }
}
