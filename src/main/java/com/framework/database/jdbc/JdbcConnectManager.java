package com.framework.database.jdbc;

import com.framework.utils.config.ProjectConfig;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;

import java.sql.*;

import static com.framework.utils.config.ConfigReader.Instance;

public class JdbcConnectManager {
    private static final ProjectConfig config = Instance();
    private static final TestLogger logger = new TestLogger(JdbcConnectManager.class);
    private static volatile JdbcConnectManager instance;
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    // Приватный конструктор для Singleton
    private JdbcConnectManager() {
        // Защита от рефлексии
        if (instance != null) {
            throw new IllegalStateException("Already initialized");
        }
    }

    // Потокобезопасное получение инстанса
    public static JdbcConnectManager getInstance() {
        JdbcConnectManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (JdbcConnectManager.class) {
            if (instance == null) {
                instance = new JdbcConnectManager();
            }
            return instance;
        }
    }

    @Step("Получить соединение с БД (JDBC)")
    public Connection getConnection() throws SQLException {
        Connection connection = connectionHolder.get();
        try {
            if (connection == null || connection.isClosed()) {
                connection = createConnection();
                connectionHolder.set(connection);
            }
        } catch (SQLException e) {
            logger.error("Failed to get database connection: {}", e.getMessage());
            throw e;
        }
        return connection;
    }

    @Step("Создать соединение с БД (JDBC)")
    private Connection createConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(
                    config.databaseUrl(),
                    config.databaseUser(),
                    config.databasePassword()
            );
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to create database connection: {}", e.getMessage());
            throw e;
        }
    }

    @Step("Закрыть соединение с БД (JDBC)")
    public void closeConnection() {
        Connection connection = connectionHolder.get();
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("Error closing connection: {}", e.getMessage());
            } finally {
                connectionHolder.remove();
            }
        }
    }

    // Метод для очистки всех соединений (может быть полезен при завершении тестов)
    public static void clearAllConnections() {
        synchronized (JdbcConnectManager.class) {
            if (instance != null) {
                instance.closeConnection();
                instance = null;
            }
        }
    }
}