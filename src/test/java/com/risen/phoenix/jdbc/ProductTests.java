package com.risen.phoenix.jdbc;

import com.risen.phoenix.jdbc.pojo.Product;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ProductTests extends BaseTest {

    @Test
    void createTable() throws SQLException {
        phoenixService.createTable(Product.class);
    }

    @Test
    void batchSave() throws SQLException {
        List<Product> list = new ArrayList<>();
        for(int i = 1; i <= 10000; i++){
            Product kingAnimal = new Product();
            kingAnimal.setUuid(Long.valueOf(i));
            kingAnimal.setProductUnLong(Long.valueOf(i*6));
            kingAnimal.setGmtCreate(new Date());
            kingAnimal.setGmtModify(new Date());
            kingAnimal.setProductBoole(new Random().nextBoolean());
            kingAnimal.setProductEmail("153872761@qq.com");
            kingAnimal.setProductFloat(new Random().nextFloat());
            kingAnimal.setProductDouble(new Random().nextDouble());
            kingAnimal.setProductName("番茄" + new Random().nextInt(10));
            list.add(kingAnimal);
        }

        int i = phoenixService.batchSave(list);
        System.out.println("成功执行数据：" + i + "条");
    }

}
