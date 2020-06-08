package com.risen.phoenix.jdbc.core;

import com.google.common.base.CaseFormat;
import com.risen.phoenix.jdbc.pojo.BasePhoenix;
import com.risen.phoenix.jdbc.table.PhoenixField;
import com.risen.phoenix.jdbc.table.PhoenixTable;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PhoenixJDBC的抽象类
 */
public abstract class AbstractPhoenixJdbc {

    /**
     *
     * @param sql 可执行SQL，增删改，建表均可
     * @throws SQLException 异常
     */
    public abstract Integer createTable(String sql) throws SQLException;

    /**
     *
     * @param clazz 源类
     * @throws SQLException 异常
     */
    public abstract Integer createTable(Class<?> clazz) throws SQLException;

    /**
     *
     * @param t 类 extends BasePhoenix
     * @throws SQLException 异常
     */
    public abstract  <T> void save(T t) throws SQLException;

    public abstract void delete() throws SQLException;

    public abstract void update() throws SQLException;

    /**
     * 查询语句
     * @param t 类
     * @throws SQLException 异常
     */
    public abstract <T> List<T> select(T t) throws SQLException;

    /**
     * 构建建表语句
     * @param tab 表信息
     * @return SQL
     */
    protected String buildCreateSql(PhoenixTable tab){
        boolean priKey = false;
        List<PhoenixField> fields = tab.getFields();
        StringBuilder sql = new StringBuilder("CREATE TABLE " + tab.getSchem() + tab.getTableName() + "(");
        StringBuilder primarySql = new StringBuilder();
        int i = 0, size = fields.size();
        for (PhoenixField field : fields) {
            sql.append(field.getFieldName()).append(" ").append(field.getFieldType()).append(" ").append(field.getCanNull() ? "NOT NULL" : "");
            if (i != size){
                sql.append(",");
            }
            if (field.getPrimaryKey()){
                priKey = true;
                primarySql.append(field.getFieldName()).append(",");
            }
            i++;
        }
        if (priKey){
            String str = primarySql.substring(0, primarySql.length() - 1);
            sql.append(buildPrimaryKeySql(str));
        }
        sql.append(")");
        return sql.toString();
    }


    /**
     * 构建插入语句
     * @param schem 库名
     * @param tableName 表名
     * @param colum 字段名
     * @param value 数值名
     * @return
     */
    protected String buildInsertSql(String schem, String tableName, String colum, String value){
        StringBuilder insert = new StringBuilder("UPSERT INTO ").append(schem);
        if (!schem.endsWith(".")){
            insert.append(".");
        }
        insert.append(tableName).append("(");
        insert.append(colum);
        insert.append(") VALUES(");
        insert.append(value);
        insert.append(")");
        return insert.toString();
    }

    /**
     * 结果集映射为对象
     * @param rs 结果集
     * @param clazz 对象
     * @param <T> 泛型
     * @return list集合
     */
    protected <T> List<T> parseObject(ResultSet rs, Class<T> clazz){
        List<T> list = new ArrayList<T>();
        Object obj=null;
        try {
            int i = 0;
            while (rs.next()) {
                obj = clazz.newInstance();
                //利用反射，获取对象类信息中的所有属性
                Field[] fields = clazz.getDeclaredFields();
                for(Field fd:fields){
                    fd.setAccessible(true);
                    Object object = rs.getObject(lowerCamel(fd.getName()));
                    fd.set(obj, object);
                }
                i++;
            }
            System.out.println("数据：---" + i + "---条");
            list.add((T)obj);
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 构建主键语句
     * @param str 字段名称集合
     */
    private StringBuffer buildPrimaryKeySql(String str){
        StringBuffer primaryKeySql = new StringBuffer();
        return primaryKeySql.append("  CONSTRAINT PK PRIMARY KEY (").append(str).append(")");
    }

    /**
     * 驼峰式命名，下划线风格并大写
     * @param str createTime
     * @return CREATE_TIME
     */
    protected String lowerCamel(String str){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str).toUpperCase();
    }

    /**
     * 传输的值处理
     * @param type java类型
     * @param data 数据值
     * @param flat 返回时追加的字符串
     */
    protected Object commaNorm(String type, Object data, String flat){
        if (data == null){
            return data;
        }
        switch (type){
            case "java.lang.String":
                return String.format("'%s'%s", data, flat);
            case "java.util.Date":
                Date date = (Date) data;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = dateFormat.format(date);
                return String.format("TO_DATE('%S')%s", format, flat);
            default:
                return data + flat;

        }
    }
}
