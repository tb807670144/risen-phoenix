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
import java.util.List;

public class PhoenixUtil<T> {

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
//       System.setProperty("hadoop.home.dir", "E:/winutils-master/hadoop-2.7.1");
        try {
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
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

    public static void main(String[] args) throws Exception {
//-----------------------------------------------------------------
        List<PhoenixField> list = new ArrayList<>();
        PhoenixField phoenixField1 = new PhoenixField("demoUuid", DbColumType.getPhoenixType("java.lang.Integer"), true);
        PhoenixField phoenixField2 = new PhoenixField("demoName", DbColumType.getPhoenixType("java.lang.String"));
        PhoenixField phoenixField3 = new PhoenixField("demoType", DbColumType.getPhoenixType("java.lang.Double"));

        list.add(phoenixField1);
        list.add(phoenixField2);
        list.add(phoenixField3);

        CreatePhoenix createPhoenix = new CreatePhoenix("DDW", list);
        String s = JSONObject.toJSONString(createPhoenix);
        System.out.println(s);
//-------------------------美丽分割线----------------------------------
        System.out.println(createPhoenix.buildCreateSQL());

//-----------------------------------------------------------------

        /*PhoenixUtil<Demo01> studentPhoenixUtil = new PhoenixUtil<>();
        studentPhoenixUtil.getConnection();

        String table = studentPhoenixUtil.createTable(Student.class);
        System.out.println("构建完毕SQL:  ");
        System.out.println(table);
//        studentPhoenixUtil.execute(table);
//        studentPhoenixUtil.executeQuery();



        studentPhoenixUtil.closeResource();
*/
//        String xiaoMiUtil = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "xiaoMiVeryGood");


    }

}
