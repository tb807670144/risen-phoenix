package com.risen.phoenix.util.common;

import com.risen.phoenix.util.pojo.Student;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoenixUtil {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private static final String DRIVER_CLASS = "org.apache.phoenix.jdbc.PhoenixDriver";
    private static final String URL = "jdbc:phoenix:bigdata:6581";

    {
        try {
            Class.forName(DRIVER_CLASS);
            System.out.println("成功加载Phoenix驱动");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获取连接
    private void getConnection() {
        // 配置系统变量
//        System.setProperty("hadoop.home.dir", "E:/winutils-master/hadoop-2.7.1");
        try {

            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(true);
            statement = connection.createStatement();

            //dosomthing
            List<Object> list = executeQuery(Student.class);
            System.out.println("查询成功！！！" + list.size());
            for (Object o : list) {
                Student student = (Student) o;
                System.out.println(student.toString());
            }



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally{
            closeResource();
        }
    }

    private void closeResource(){
        try {
            if (resultSet != null){
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object> executeQuery(Class<?> clazz) throws Exception{
        List<Object> list = new ArrayList<>();
        String sql = getSql();
        resultSet = statement.executeQuery(sql);
        Object obj = null;
        while (resultSet.next()){
            System.out.println(resultSet.getInt("id") + "--------" + resultSet.getString("name"));
            obj = clazz.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++){
                String columnClassName = metaData.getColumnClassName(i);
                System.out.println("数据类型：" + columnClassName);
                String catalogName = metaData.getColumnName(i).toLowerCase();
                Field f = obj.getClass().getDeclaredField(catalogName);
                f.setAccessible(true);
                f.set(obj, resultSet.getObject(catalogName));
            }
            list.add(obj);
        }
        return list;
//          statement.execute(sql);
    }

    public String getSql(){
        String createTable = "create table if not exists TEST.STUDENT(id integer primary key,name varchar(20))";
        String select = "select * from TEST.STUDENT";
//        String insert = "upsert into TEST.STUDENT values(4,'小狗')";
        return select;
    }

    public static void main(String[] args) throws Exception {

        PhoenixUtil phoenixUtil = new PhoenixUtil();
        phoenixUtil.getConnection();

    }

}
