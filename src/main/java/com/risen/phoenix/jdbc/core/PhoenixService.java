package com.risen.phoenix.jdbc.core;

import com.risen.phoenix.jdbc.annotations.PhxField;
import com.risen.phoenix.jdbc.annotations.PhxId;
import com.risen.phoenix.jdbc.annotations.PhxTabName;
import com.risen.phoenix.jdbc.core.enums.PhxDataTypeEnum;
import com.risen.phoenix.jdbc.table.PhoenixField;
import com.risen.phoenix.jdbc.table.PhoenixTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 简化Phoenix
 * 绑定参数执行SQL,POJO对象结果映射
 */
@Service
public class PhoenixService extends AbstractPhoenixJdbc{

    @Autowired(required = false)
    @Qualifier("phoenTemplate")
    JdbcTemplate jdbcTemplate;

    /**
     * 直接执行SQL
     * @param sql 建表语句
     * @return Integer
     * @throws SQLException 异常信息
     */
    @Override
    public Integer createTable(String sql) throws SQLException{
        jdbcTemplate.execute(sql);
        return 0;
    }

    /**
     * 根据类创建表
     * @param clazz 泛型
     * @return Integer
     * @throws SQLException 异常信息
     */
    @Transactional
    @Override
    public Integer createTable(Class<?> clazz) throws SQLException{
        String tableName = null, schem = null;
        int salt = 3;
        boolean bar1 = clazz.isAnnotationPresent(PhxTabName.class), compression = false;
        if (bar1){
            PhxTabName an1 = clazz.getAnnotation(PhxTabName.class);
            if (an1.upLower()){
                tableName = an1.tableName().toUpperCase();
                schem = "".equals(an1.schem()) ? "RISEN" : an1.schem().toUpperCase();
            }else {
                tableName = lowerCamel(tableName);
                schem = "".equals(an1.schem()) ? "RISEN" : an1.schem();
            }
            salt=an1.salt();
            compression=an1.compression();
        }else {
            tableName = lowerCamel(clazz.getSimpleName());
            schem = "RISEN";
        }

        if (!schem.endsWith(".")){
            schem = schem + ".";
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
        PhoenixTable phoenixTable = new PhoenixTable(schem, tableName, list, salt, compression);
        String sql = buildCreateSql(phoenixTable);
        System.out.println(sql);
        return createTable(sql);
    }

    @Transactional(rollbackFor = SQLException.class)
    @Override
    public <T> int save(T t) throws SQLException{
        String sql = buildInsertSql(t);
        return jdbcTemplate.update(sql);
    }

    @Override
    public <T> int batchSave(List<T> list) throws SQLException{
        Connection conn = jdbcTemplate.getDataSource().getConnection();
        conn.setAutoCommit(false);
        int batchSize = 0, commitSize = 130, insertCount = 0;
        for (T t : list) {
            PreparedStatement statement = conn.prepareStatement(buildInsertSql(t));
            System.out.println(buildInsertSql(t));
            statement.execute();
            batchSize++;
            if (batchSize % commitSize == 0){
                batchSize = 0;
                conn.commit();
            }
            insertCount++;
        }
        conn.commit();
        return insertCount;
    }

    @Override
    public void delete() throws SQLException{

    }

    @Override
    public void update() throws SQLException{

    }

    @Override
    public <T> List<T> select(Class<T> clazz) throws SQLException {
        BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<>(clazz);
        return jdbcTemplate.query(buildSelectSql(clazz, ""), rowMapper);
    }

}
