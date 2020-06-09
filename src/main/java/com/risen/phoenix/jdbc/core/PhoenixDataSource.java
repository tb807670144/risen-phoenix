package com.risen.phoenix.jdbc.core;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class PhoenixDataSource {

    @Autowired
    private Environment environment;

    @Bean(name = "phoenixDataSourceJdbc")
    @Qualifier("phoenixDataSourceJdbc")
    public DataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(environment.getProperty("phoenix.url"));
        druidDataSource.setDriverClassName(environment.getProperty("phoenix.driver-class-name"));
        druidDataSource.setUsername(environment.getProperty("phoenix.username"));
        druidDataSource.setPassword(environment.getProperty("phoenix.password"));
        druidDataSource.setDefaultAutoCommit(Boolean.valueOf(environment.getProperty("phoenix.default-auto-commit")));
        return druidDataSource;
    }

    @Bean(name = "phoenTemplate")
    @Qualifier("phoenTemplate")
    public JdbcTemplate phoenTemplate(@Qualifier("phoenixDataSourceJdbc") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
