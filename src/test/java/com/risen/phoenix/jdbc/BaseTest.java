package com.risen.phoenix.jdbc;

import com.risen.phoenix.jdbc.core.IPhoenixService;
import com.risen.phoenix.jdbc.core.PhoenixServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class BaseTest {

    @Autowired
    IPhoenixService phoenixService;

    @BeforeEach
    public void before(){
        System.out.println("#################开始测试#################");
    }
    @AfterEach
    public void after(){
        System.out.println("#################结束测试#################");
    }
}
