package com.risen.phoenix.jdbc.core;

import com.risen.phoenix.jdbc.core.pnd.Pnd;

import java.sql.SQLException;
import java.util.List;

public interface IPhoenixService {

    /**
     * 创建表
     * @param sql 可执行SQL，增删改，建表均可
     * @throws SQLException 异常
     */
    void createTable(String sql) throws SQLException;

    /**
     * 创建表
     * @param clazz 源类
     * @throws SQLException 异常
     */
    void createTable(Class<?> clazz) throws SQLException;

    /**
     * 插入语句
     * @param t 类 extends BasePhoenix
     * @throws SQLException 异常
     */
    <T> int save(T t) throws SQLException;

    /**
     * 批量插入数据
     * @param list 集合
     * @return 插入数量
     * @throws SQLException
     */
    <T> int batchSave(List<T> list) throws SQLException;

    void delete() throws SQLException;

    void update() throws SQLException;

    /**
     * 查询语句
     * @param clazz 类
     * @throws SQLException 异常
     */
    <T> List<T> select(Class<T> clazz, Pnd<T> pnd) throws SQLException;
}
