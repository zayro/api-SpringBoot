package com.rest.api.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConnection {

    @Autowired
    private DatabaseConfig databaseConfig;

    @Bean
    public DataSource dataSourcePdn() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
 
        dataSource.setUrl(databaseConfig.getUrl());
        dataSource.setUsername(databaseConfig.getUsername());
        dataSource.setPassword(databaseConfig.getPassword());
        return dataSource;
    }

    @Bean(name = "jdbcTemplate", autowire = Autowire.BY_NAME)
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSourcePdn());
    }


}
