package com.example.productapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;

@Configuration
@EntityScan({"com.example.*"})
@EnableJpaRepositories(value = {"com.example.*"})
@EnableAutoConfiguration
@Slf4j
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        log.info("DataSource is building");
        return DataSourceBuilder.create().build();
    }

    @Bean("transactionManager")
    public JpaTransactionManager transactionManager() {
        log.info("Transaction manager is building");
        return new JpaTransactionManager();
    }
}
