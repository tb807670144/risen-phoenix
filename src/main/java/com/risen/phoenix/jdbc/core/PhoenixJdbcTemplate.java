package com.risen.phoenix.jdbc.core;

import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxId;
import com.risen.phoenix.jdbc.annotations.PhxTabName;
import com.risen.phoenix.jdbc.annotations.PhxTransaction;
import com.risen.phoenix.jdbc.core.enums.PhxDataTypeEnum;
import com.risen.phoenix.jdbc.pojo.BasePhoenix;
import com.risen.phoenix.jdbc.table.PhoenixField;
import com.risen.phoenix.jdbc.table.PhoenixTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 简化Phoenix
 * 绑定参数执行SQL,POJO对象结果映射
 */
@Component
public class PhoenixJdbcTemplate extends AbstractPhoenixJdbc{

    @Autowired
    PhoenixTransactionManager transactionManager;

    /**
     * 直接执行SQL
     * @param sql 建表语句
     * @return Integer
     * @throws SQLException 异常信息
     */
    @Override
    public Integer createTable(String sql) throws SQLException{
        return transactionManager.execute(sql);
    }

    /**
     * 根据类创建表
     * @param clazz 泛型
     * @return Integer
     * @throws SQLException 异常信息
     */
    @PhxTransaction
    @Override
    public Integer createTable(Class<?> clazz) throws SQLException{
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

        List<PhoenixField> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean bar2 = field.isAnnotationPresent(PhxId.class);
            if (bar2){
                PhxId an2 = field.getAnnotation(PhxId.class);
                PhxDataTypeEnum phxEnum = an2.value();
                int length = an2.length();
                String columnType = phxEnum.getColumnType() + (length <= 0 ? "" : "("+length+")");
                String fieldName = lowerCamel(field.getName());
                PhoenixField phoenixField = new PhoenixField(fieldName, columnType, true);
                list.add(phoenixField);
                continue;
            }
            boolean bar3 = field.isAnnotationPresent(PhxField.class);
            if (bar3){
                PhxField an3 = field.getAnnotation(PhxField.class);
                PhxDataTypeEnum phxEnum = an3.value();
                String columnType = phxEnum.getColumnType() + (an3.length() <= 0 ? "" : "("+an3.length()+")");
                String fieldName = lowerCamel(field.getName());
                PhoenixField phoenixField = new PhoenixField(fieldName, columnType);
                list.add(phoenixField);
            }
        }
        PhoenixTable phoenixTable = new PhoenixTable(schem, tableName, list);
        String sql = buildCreateSql(phoenixTable);
        return createTable(sql);
    }

    @PhxTransaction
    @Override
    public <T> void save(T t) throws SQLException{
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
                    Object result = commaNorm(field.getType().getName(), field.get(t), ",");
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
                    Object result = commaNorm(field.getType().getName(), field.get(t), ",");
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
        String sql = buildInsertSql(schem, tableName, colum, value);
        transactionManager.execute(sql);
    }

    @Override
    public void delete() throws SQLException{

    }

    @Override
    public void update() throws SQLException{

    }

    @Override
    public <T> List<T> select(T t) throws SQLException {
        ResultSet resultSet = transactionManager.executeQuery("SELECT * FROM CCT.APPLE");
        List<?> objects = parseObject(resultSet, t.getClass());
        System.out.println("结果数：" + objects.size());
        return null;
    }

}
