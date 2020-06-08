package com.risen.phoenix.jdbc;

import com.risen.phoenix.jdbc.pojo.Apple;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class RisenPhoenixApplicationTests extends BaseTest {

    @Test
    void createTable() throws SQLException {
        phoenixJdbcTemplate.createTable(Apple.class);
    }

    @Test
    void save() throws SQLException {
        Apple apple = new Apple();
        apple.setUuid(2);
        apple.setaDouble(6.6);
        apple.setaFloat(3.14f);
        apple.setName("抖音家庭");
        phoenixJdbcTemplate.save(apple);
    }

    @Test
    void select() throws SQLException {
        Apple apple = new Apple();
        phoenixJdbcTemplate.select(apple);
    }



}
