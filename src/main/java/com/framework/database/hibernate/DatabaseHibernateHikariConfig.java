package com.framework.database;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.utils.config.ProjectConfig;
import com.framework.utils.logger.TestLogger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

import static com.framework.utils.config.ConfigReader.Instance;

public class DatabaseHibernateHikariConfig {
    private static final TestLogger logger = new TestLogger(DatabaseHibernateHikariConfig.class);
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    public static void initDatabase() {
        try {
            Configuration configuration = new Configuration();

            ProjectConfig config = Instance();
            // Базовые настройки Hibernate
            Properties settings = new Properties();
            settings.put(Environment.DRIVER, "org.postgresql.Driver");
            settings.put(Environment.URL, config.databaseUrl());
            settings.put(Environment.USER, config.databaseUser());
            settings.put(Environment.PASS, config.databasePassword());
            settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
            settings.put(Environment.SHOW_SQL, "true");
            settings.put(Environment.FORMAT_SQL, "true");
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            settings.put(Environment.HBM2DDL_AUTO, "update");

            // Настройки пула соединений
            settings.put(Environment.CONNECTION_PROVIDER, "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
            settings.put("hibernate.hikari.maximumPoolSize", "10");
            settings.put("hibernate.hikari.minimumIdle", "5");
            settings.put("hibernate.hikari.idleTimeout", "300000");
            settings.put("hibernate.hikari.connectionTimeout", "20000");

            configuration.setProperties(settings);
            configuration.addAnnotatedClass(CreateUserPojoRq.class);

            registry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(registry);

        } catch (Exception e) {
            logger.error("Failed to create SessionFactory", e);
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
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
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
