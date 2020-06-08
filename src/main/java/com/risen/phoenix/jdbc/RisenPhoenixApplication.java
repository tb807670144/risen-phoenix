package com.risen.phoenix.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class RisenPhoenixApplication {

    public static void main(String[] args) {
        SpringApplication.run(RisenPhoenixApplication.class, args);
    }

}
