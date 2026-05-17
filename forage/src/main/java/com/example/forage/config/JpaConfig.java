package com.example.forage.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.forage.repository")
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.example.forage")
public class JpaConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {

        HikariDataSource ds = new HikariDataSource();

        ds.setDriverClassName(env.getProperty("db.driver"));
        ds.setJdbcUrl(env.getProperty("db.url"));
        ds.setUsername(env.getProperty("db.username"));
        ds.setPassword(env.getProperty("db.password"));

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource());
        em.setPackagesToScan("com.example.forage.entity");

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();

        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());

        return em;
    }

    private Properties hibernateProperties() {

        Properties properties = new Properties();

        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));

        properties.put("hibernate.show_sql",
                env.getProperty("hibernate.show_sql"));

        properties.put("hibernate.format_sql",
                env.getProperty("hibernate.format_sql"));

        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));

        return properties;
    }

    @Bean
    public JpaTransactionManager transactionManager(
            EntityManagerFactory emf) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}