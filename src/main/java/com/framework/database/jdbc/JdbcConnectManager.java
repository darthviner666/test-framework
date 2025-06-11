package com.framework.database.jdbc;

import com.framework.utils.config.ProjectConfig;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;

import java.sql.*;

import static com.framework.utils.config.ConfigReader.Instance;

/**
 * Менеджер соединений с базой данных через JDBC.
 * Реализует паттерн Singleton для обеспечения единственного экземпляра менеджера соединений.
 * Использует ThreadLocal для хранения соединений, что обеспечивает потокобезопасность.
 */
public class JdbcConnectManager {
    /** Конфигурация проекта для получения параметров подключения к БД */
    private static final ProjectConfig config = Instance();
    
    /** Логгер для записи информации о работе менеджера соединений */
    private static final TestLogger logger = new TestLogger(JdbcConnectManager.class);
    
    /** Единственный экземпляр менеджера соединений (Singleton) */
    private static volatile JdbcConnectManager instance;
    
    /** Хранилище соединений для каждого потока */
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    /**
     * Приватный конструктор для реализации паттерна Singleton.
     * Содержит защиту от создания экземпляра через рефлексию.
     * 
     * @throws IllegalStateException если экземпляр уже был создан
     */
    private JdbcConnectManager() {
        // Защита от рефлексии
        if (instance != null) {
            throw new IllegalStateException("Уже инициализирован");
        }
    }

    /**
     * Потокобезопасное получение экземпляра менеджера соединений.
     * Реализует паттерн Double-Checked Locking для оптимизации производительности.
     * 
     * @return экземпляр JdbcConnectManager
     */
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

    /**
     * Получает соединение с базой данных для текущего потока.
     * Если соединение отсутствует или закрыто, создает новое.
     * 
     * @return активное соединение с базой данных
     * @throws SQLException если не удалось получить соединение
     */
    @Step("Получить соединение с БД (JDBC)")
    public Connection getConnection() throws SQLException {
        Connection connection = connectionHolder.get();
        try {
            if (connection == null || connection.isClosed()) {
                connection = createConnection();
                connectionHolder.set(connection);
            }
        } catch (SQLException e) {
            logger.error("Не удалось получить соединение с базой данных: {}", e.getMessage());
            throw e;
        }
        return connection;
    }

    /**
     * Создает новое соединение с базой данных.
     * Устанавливает режим автоматической фиксации транзакций в false.
     * 
     * @return новое соединение с базой данных
     * @throws SQLException если не удалось создать соединение
     */
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
            logger.error("Не удалось создать соединение с базой данных: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Закрывает соединение с базой данных для текущего потока.
     * Удаляет соединение из ThreadLocal после закрытия.
     */
    @Step("Закрыть соединение с БД (JDBC)")
    public void closeConnection() {
        Connection connection = connectionHolder.get();
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("Ошибка при закрытии соединения: {}", e.getMessage());
            } finally {
                connectionHolder.remove();
            }
        }
    }

    /**
     * Очищает все соединения и сбрасывает экземпляр менеджера.
     * Полезно вызывать при завершении тестов для освобождения ресурсов.
     */
    public static void clearAllConnections() {
        synchronized (JdbcConnectManager.class) {
            if (instance != null) {
                instance.closeConnection();
                instance = null;
            }
        }
    }
}