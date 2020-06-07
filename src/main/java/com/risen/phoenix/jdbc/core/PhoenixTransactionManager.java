package com.risen.phoenix.jdbc.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class PhoenixTransactionManager {

    @Autowired(required = false)
    DataSource dataSource;

    {
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
//            System.setProperty("hadoop.home.dir", "E:/winutils-master/hadoop-3.0.0");
            System.out.println("成功加载Phoenix驱动");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ThreadLocal<Connection> connection = new ThreadLocal<>();
    private ThreadLocal<Statement> statement = new ThreadLocal<>();

    public Connection getConnection() throws SQLException {
        if (connection.get() == null){
            connection.set(DriverManager.getConnection("jdbc:phoenix:bigdata:6581", "", ""));
            /*if (dataSource == null){
                throw new RuntimeException("异常：未配置Phoenix数据源");
            }*/
//            connection.set(dataSource.getConnection());
        }
        Connection connection = this.connection.get();
        Statement sta = connection.createStatement();
        statement.set(sta);
        return connection;
    }

    public Integer execute(String sql) throws SQLException{
        Statement statement = getConnection().createStatement();
        int i = statement.executeUpdate(sql);
        connection.get().commit();
        return i;
    }

    public ResultSet executeQuery(String sql) throws SQLException{
        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        connection.get().commit();
        return resultSet;
    }

    public void closeResource() throws SQLException{
        closeResource(null);
    }

    public void closeResource(ResultSet resultSet) throws SQLException{
        if (resultSet != null){
            resultSet.close();
        }
        statement.get().close();
        connection.get().close();
        connection.remove();
        statement.remove();
    }
}
