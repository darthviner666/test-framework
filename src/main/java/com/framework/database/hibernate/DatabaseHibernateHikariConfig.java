package com.framework.database.hibernate;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.database.tables.User;
import com.framework.utils.config.ProjectConfig;
import com.framework.utils.logger.TestLogger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

import static com.framework.utils.config.ConfigReader.Instance;

/**
 * Конфигурация Hibernate с использованием пула соединений HikariCP.
 * Класс предоставляет методы для инициализации, получения и закрытия фабрики сессий Hibernate.
 */
public class DatabaseHibernateHikariConfig {
    /** Логгер для записи информации о работе с базой данных */
    private static final TestLogger logger = new TestLogger(DatabaseHibernateHikariConfig.class);
    /** Фабрика сессий Hibernate */
    private static SessionFactory sessionFactory;
    /** Реестр стандартных сервисов Hibernate */
    private static StandardServiceRegistry registry;

    /**
     * Инициализирует базу данных, создавая фабрику сессий Hibernate.
     * Настраивает подключение к базе данных и пул соединений HikariCP.
     * 
     * @throws RuntimeException если не удалось создать фабрику сессий
     */
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
            logger.error("Не удалось создать SessionFactory", e);
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
            throw new RuntimeException("Не удалось создать SessionFactory", e);
        }
    }

    /**
     * Возвращает фабрику сессий Hibernate.
     * Если фабрика еще не создана, инициализирует базу данных.
     * 
     * @return фабрика сессий Hibernate
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            initDatabase();
        }
        return sessionFactory;
    }

    /**
     * Закрывает фабрику сессий и освобождает ресурсы.
     * Должен вызываться при завершении работы с базой данных.
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
