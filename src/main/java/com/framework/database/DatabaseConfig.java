package com.framework.database;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.utils.config.ProjectConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Properties;

import static com.framework.utils.config.ConfigReader.Instance;

public class DatabaseConfig {
    private static HikariDataSource dataSource;
    private static SessionFactory sessionFactory;

    public static void initDatabase() {
        HikariConfig config = getHikariConfig();

        dataSource = new HikariDataSource(config);

        Properties hibernateProps = new Properties();
        hibernateProps.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProps.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProps.setProperty("hibernate.show_sql", "true");
        hibernateProps.setProperty("hibernate.format_sql", "true");

        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(hibernateProps)
                    .applySetting("hibernate.connection.datasource", dataSource)
                    .build();

            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(CreateUserPojoRq.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SessionFactory", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            initDatabase();
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (dataSource != null) {
            dataSource.close();
        }
    }

    private static HikariConfig getHikariConfig() {
        ProjectConfig configReader = Instance();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(configReader.databaseUrl());
        config.setUsername(configReader.databaseUser());
        config.setPassword(configReader.databasePassword());

        // Дополнительные настройки HikariCP
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(20000);
        config.setMaxLifetime(1200000);
        return config;
    }

}
