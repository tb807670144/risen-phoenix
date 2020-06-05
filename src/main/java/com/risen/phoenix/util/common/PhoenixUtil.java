package com.risen.phoenix.util.common;

import com.alibaba.fastjson.JSONObject;
import com.risen.phoenix.util.common.table.CreatePhoenix;
import com.risen.phoenix.util.common.table.PhoenixField;
import com.risen.phoenix.util.pojo.Demo01;
import com.risen.phoenix.util.pojo.Student;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoenixUtil<T> {

    private static Connection connection;
    private static Statement statement;
    private  ResultSet resultSet;

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
        System.setProperty("hadoop.home.dir", "D:/winutils-master/hadoop-2.7.1");
        System.setProperty("HADOOP_HOME", "D:/winutils-master/hadoop-2.7.1");
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
        }
    }

    /**
     * 关闭资源连接
     */
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

    /**
     * 支持简单的直接将对象插入Hbase
     * @param clazz
     */
    public List<Object> executeQuery(Class<T> clazz) throws Exception{
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
    }


    /**
     * 执行JSON格式
     * @param json
     */
    public List<Object> executeQuery(String json) throws Exception{
        CreatePhoenix createPhoenix = JSONObject.parseObject(json, CreatePhoenix.class);
        JSONObject jsonObj = JSONObject.parseObject(json);
        Assert.assertNotNull(createPhoenix.getTableName());
        Assert.assertNotNull(createPhoenix.getFields());
        if (!jsonObj.containsKey("tableName")){
            throw new RuntimeException("json必须包含tableName");
        }
        if (!jsonObj.containsKey("fields")){
            throw new RuntimeException("json必须包含fields属性字段信息");
        }
        String sql = createPhoenix.buildCreateSQL();
        List<Object> list = new ArrayList<>();
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            System.out.println(resultSet.getInt("id") + "--------" + resultSet.getString("name"));

        }
        return null;
    }


    public boolean execute(String sql) throws Exception{
        return statement.execute(sql);
    }


    /**
     * 根据类进行创建表
     * 规则：
     */
    public String createTable(Class<?> clazz){
        List<PhoenixField> list = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            PhoenixField field1 = new PhoenixField();
            field1.setFieldName(field.getName());
            field1.setFieldType(DbColumType.getPhoenixType(field.getType().getName()));

            list.add(field1);
        }
        CreatePhoenix createPhoenix = new CreatePhoenix(clazz.getSimpleName(), list);
        return createPhoenix.buildCreateSQL();
    }

    public String getSql(){
        String createTable = "create table if not exists TEST.STUDENT(id integer primary key,name varchar(20))";
        String select = "select * from TEST.STUDENT";
//        String insert = "upsert into TEST.STUDENT values(4,'小狗')";
        return select;
    }
    /**
     * 获取Phoenix中的表(系统表除外)
     */
    public static List<String> getTables() throws Exception {
        List<String> tables = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        String[] types = {"TABLE"}; //"SYSTEM TABLE"
        ResultSet resultSet = metaData.getTables(null, null, null, types);
        while (resultSet.next()) {
            tables.add(resultSet.getString("TABLE_NAME"));
        }
        return tables;
    }

    /**
     * 获取表中的所有数据
     */
    public List<Map<String, String>> getList(String tableName) throws Exception {
        String sql = "SELECT * FROM " + tableName;
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        List<Map<String, String>> resultList = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, String> result = new HashMap<>();
            for (int i = 1, len = resultSetMetaData.getColumnCount(); i <= len; i++) {
                result.put(resultSetMetaData.getColumnName(i), resultSet.getString(i));
            }
            resultList.add(result);
        }
        return resultList;
    }
    /**
     * 新增数据
     */
    public static boolean saveData() throws Exception {
        String sql = "upsert into TEST.TOKOYHOT(ID,NAME,AGE) values(3,'波多野结衣','18')";
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return statement.execute(sql);
    }

    public static void main(String[] args) throws Exception {

        PhoenixUtil phoenixUtil = new PhoenixUtil();
        phoenixUtil.getConnection();
        System.out.println(saveData());
        phoenixUtil.closeResource();
    }

}
