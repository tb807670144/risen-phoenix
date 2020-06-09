package com.risen.phoenix.jdbc;

import com.risen.phoenix.jdbc.pojo.Apple;
import com.risen.phoenix.jdbc.pojo.KingAnimal;
import com.risen.phoenix.jdbc.pojo.Student;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

class RisenPhoenixApplicationTests extends BaseTest {

    @Test
    void createTable() throws SQLException {
        phoenixService.createTable(KingAnimal.class);
    }

    @Test
    void save() throws SQLException {
        List<Apple> list = new ArrayList<>();
        Apple apple = new Apple();
        apple.setUuid(4);
        apple.setaDouble(6.6);
        apple.setaFloat(3.14f);
        apple.setGmtCreate(new Date());
        apple.setName("小二郎");
        Apple apple2 = new Apple();
        apple2.setUuid(5);
        apple2.setaDouble(80.8);
        apple2.setaFloat(3.14f);
        apple2.setGmtCreate(new Date());
        apple2.setName("大姐头");
        list.add(apple);
        list.add(apple2);
        phoenixService.save(apple2);
    }

    @Test
    void batchSave() throws SQLException {
        List<KingAnimal> list = new ArrayList<>();
        for(int i = 1; i < 1000; i++){
            KingAnimal kingAnimal = new KingAnimal();
            kingAnimal.setKingUuid(i);
            kingAnimal.setKingName("动物"+i*3);
            kingAnimal.setKingType(new Random().nextInt()+"");
            list.add(kingAnimal);
        }

        int i = phoenixService.batchSave(list);
        System.out.println("成功执行数据：" + i + "条");
    }

    @Test
    void select() throws SQLException {

        List<Apple> select = phoenixService.select(Apple.class);
        for (Apple apple1 : select) {
            System.out.println(apple1.toString());
        }
    }



}
