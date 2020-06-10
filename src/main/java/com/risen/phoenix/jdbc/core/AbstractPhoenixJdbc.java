package com.risen.phoenix.jdbc.core;

import com.google.common.base.CaseFormat;
import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxId;
import com.risen.phoenix.jdbc.annotations.PhxTabName;
import com.risen.phoenix.jdbc.table.PhoenixField;
import com.risen.phoenix.jdbc.table.PhoenixTable;
import org.apache.phoenix.util.QueryUtil;

import java.lang.reflect.Field;
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
     * 创建表
     * @param sql 可执行SQL，增删改，建表均可
     * @throws SQLException 异常
     */
    public abstract Integer createTable(String sql) throws SQLException;

    /**
     * 创建表
     * @param clazz 源类
     * @throws SQLException 异常
     */
    public abstract Integer createTable(Class<?> clazz) throws SQLException;

    /**
     * 插入语句
     * @param t 类 extends BasePhoenix
     * @throws SQLException 异常
     */
    public abstract <T> int save(T t) throws SQLException;

    /**
     * 批量插入数据
     * @param list 集合
     * @return 插入数量
     * @throws SQLException
     */
    public abstract <T> int batchSave(List<T> list) throws SQLException;

    public abstract void delete() throws SQLException;

    public abstract void update() throws SQLException;

    /**
     * 查询语句
     * @param clazz 类
     * @throws SQLException 异常
     */
    public abstract <T> List<T> select(Class<T> clazz) throws SQLException;

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
        if (tab.isCompression()){
            sql.append(" COMPRESSION = 'GZ', ");
        }
        sql.append("  SALT_BUCKETS = ").append(tab.getSalt());
        return sql.toString();
    }

    protected <T> String buildInsertSql(T t){
        Class<?> clazz = t.getClass();
        String tableName = null, schem = null;
        boolean bar1 = clazz.isAnnotationPresent(PhxTabName.class);
        if (bar1){
            PhxTabName an1 = clazz.getAnnotation(PhxTabName.class);
            if (an1.upLower()){
                tableName = an1.tableName().toUpperCase();
                schem = "".equals(an1.schem()) ? "RISEN" : an1.schem().toUpperCase();
            }else {
                tableName = lowerCamel(tableName);
                schem = "".equals(an1.schem()) ? "RISEN" : an1.schem();
            }
        }else {
            tableName = lowerCamel(clazz.getSimpleName());
            schem = "RISEN";
        }

        StringBuilder fieldSql = new StringBuilder();
        StringBuilder valueSql = new StringBuilder();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean bar2 = field.isAnnotationPresent(PhxId.class);
            if (bar2){
                try {
                    field.setAccessible(true);
                    Object result = commaNorm(field.getType().getSimpleName(), field.get(t), ",");
                    if (result != null){
                        fieldSql.append(lowerCamel(field.getName())).append(",");
                        valueSql.append(result);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                continue;
            }
            boolean bar3 = field.isAnnotationPresent(PhxField.class);
            if (bar3){
                try {
                    field.setAccessible(true);
                    Object result = commaNorm(field.getType().getSimpleName(), field.get(t), ",");
                    if (result != null){
                        fieldSql.append(lowerCamel(field.getName())).append(",");
                        valueSql.append(result);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        String colum = fieldSql.substring(0, fieldSql.length() - 1);
        String value = valueSql.substring(0, valueSql.length() - 1);
        return buildInsertSql(schem, tableName, colum, value);
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
     * 构建主键语句
     * @param str 字段名称集合
     */
    private StringBuffer buildPrimaryKeySql(String str){
        StringBuffer primaryKeySql = new StringBuffer();
        return primaryKeySql.append("  CONSTRAINT PK PRIMARY KEY (").append(str).append(")");
    }

    /**
     * 驼峰式命名，下划线风格并大写
     * createTime 转 CREATE_TIME
     * @param str createTime
     * @return CREATE_TIME
     */
    protected static String lowerCamel(String str){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str).toUpperCase();
    }

    /**
     * 驼峰式命名，转java风格
     * CREATE_TIME 转 createTime
     * @param str CREATE_TIME
     * @return createTime
     */
    protected static String camelLower(String str){
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }

    /**
     * 查询条件
     * @return
     */
    protected <T> String buildSelectSql(Class<T> clazz, String where){
        String tableName, schem;
        boolean bar1 = clazz.isAnnotationPresent(PhxTabName.class);
        if (bar1) {
            PhxTabName annotation = clazz.getAnnotation(PhxTabName.class);
            schem = annotation.schem();
            if (!schem.endsWith(".")){
                schem = schem + ".";
            }
            tableName = schem + annotation.tableName();
        }else {
            tableName = "RISEN." + lowerCamel(clazz.getSimpleName());
        }

        List<String> strList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean bar2 = field.isAnnotationPresent(PhxId.class);
            if (bar2){
                strList.add(lowerCamel(field.getName()));
                continue;
            }
            boolean bar3 = field.isAnnotationPresent(PhxField.class);
            if (bar3){
                strList.add(lowerCamel(field.getName()));
            }
        }

        return QueryUtil.constructSelectStatement(tableName, strList, where, null, false);
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
            case "String":
                return String.format("'%s'%s", data, flat);
            case "Date":
                Date date = (Date) data;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = dateFormat.format(date);
                return String.format("TO_DATE('%s')%s", format, flat);
            default:
                return data + flat;

        }
    }

}
