package com.risen.phoenix.jdbc.core;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class PhoenixDataSource {

    @Value("${phoenix.url:}")
    String url;
    @Value("${phoenix.driver-class-name:org.apache.phoenix.jdbc.PhoenixDriver}")
    String driverClassName;
    @Value("${phoenix.username:}")
    String userName;
    @Value("${phoenix.password:}")
    String passWord;
    @Value("${phoenix.default-auto-commit:false}")
    String autoCommit;

    @Bean(name = "phoenixDataSourceJdbc")
    @Qualifier("phoenixDataSourceJdbc")
    public DataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(passWord);
        druidDataSource.setDefaultAutoCommit(Boolean.valueOf(autoCommit));
        return druidDataSource;
    }

    @Bean(name = "phoenTemplate")
    @Qualifier("phoenTemplate")
    public JdbcTemplate phoenTemplate(@Qualifier("phoenixDataSourceJdbc") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
