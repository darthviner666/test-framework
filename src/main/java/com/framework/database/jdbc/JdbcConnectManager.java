package com.framework.database.jdbc;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.utils.config.ProjectConfig;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;

import java.sql.*;

import static com.framework.utils.config.ConfigReader.Instance;

public class JdbcActions  {
    protected static final ProjectConfig config = Instance();
    protected static final TestLogger logger = new TestLogger(JdbcActions.class);
    protected static Connection connection;
    protected static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public JdbcActions() throws SQLException {
        getConnection();
    }

    @Step("Получить соединение с БД (JDBC)")
    protected Connection getConnection() throws SQLException {
        connection = connectionHolder.get();
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
    private  Connection createConnection()  throws SQLException {
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


}