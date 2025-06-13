package com.framework.database.jdbc.users;

import com.framework.database.jdbc.JdbcConnectManager;
import com.framework.database.jdbc.repositories.Repository;
import com.framework.database.tables.User;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryJdbc extends Repository<User> {
    /** Логгер для записи информации о работе с базой данных */
    private static final TestLogger logger = new TestLogger(UserRepositoryJdbc.class);
    /** Менеджер соединений с базой данных */
    private final JdbcConnectManager jdbcManager;
    /** Флаг для включения/отключения логирования SQL-запросов */
    private final boolean logSql = true;
    /**
     * Создает новый экземпляр репозитория и инициализирует таблицу пользователей.
     *
     * @throws SQLException если произошла ошибка при подключении к базе данных
     */
    public UserRepositoryJdbc() throws SQLException {
        this.jdbcManager = JdbcConnectManager.getInstance();
        this.tableName = "USERS";
        createTableIfNotExists();
    }

    /**
     * Получает соединение с базой данных.
     *
     * @return активное соединение с базой данных
     * @throws SQLException если не удалось получить соединение
     */
    private Connection getConnection() throws SQLException {
        return jdbcManager.getConnection();
    }

    /**
     * Логирует SQL-запрос и его параметры.
     *
     * @param sql SQL-запрос для логирования
     * @param params параметры запроса
     */
    private void logSql(String sql, Object... params) {
        if (logSql) {
            StringBuilder logMessage = new StringBuilder("SQL: ").append(sql);
            if (params != null && params.length > 0) {
                logMessage.append(" | Params: ");
                for (int i = 0; i < params.length; i++) {
                    logMessage.append(i + 1).append("=").append(params[i]);
                    if (i < params.length - 1) {
                        logMessage.append(", ");
                    }
                }
            }
            logger.info(logMessage.toString());
        }
    }

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь или null, если пользователь не найден
     */
    @Override
    @Step("Получить пользователя из базы")
    public User findById(Long id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        logSql(sql, id);
        Allure.addAttachment("SQL Запрос:", sql);
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return User.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .job(rs.getString("job"))
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.error("Не удалось получить пользователя: {}", e.getMessage());
            throw new RuntimeException("Не удалось получить пользователя", e);
        }
        return null;
    }

    /**
     * Сохраняет нового пользователя в базе данных.
     *
     * @param entity пользователь для сохранения
     * @return сохраненный пользователь с присвоенным идентификатором
     * @throws SQLException если произошла ошибка при сохранении
     */
    @Override
    @Step("Создать пользователя в базе данных")
    public User save(User entity) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (name, job) VALUES (?, ?)";
        logSql(sql, entity.getName(), entity.getJob());
        Allure.addAttachment("SQL Запрос:", sql);
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getJob());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }

            connection.commit();
            logger.info("Успешно создан пользователь с ID: {}", entity.getId());
            return entity;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Ошибка при откате транзакции: {}", rollbackEx.getMessage());
            }
            logger.error("Не удалось создать пользователя: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Обновляет существующего пользователя в базе данных.
     *
     * @param entity пользователь для обновления
     * @return обновленный пользователь или null, если пользователь не найден
     */
    @Override
    @Step("Обновить пользователя в базе данных")
    public User update(User entity) {
        String sql = "UPDATE " + tableName + " SET name = ?, job = ? WHERE id = ?";
        logSql(sql, entity.getName(), entity.getJob(), entity.getId());
        Allure.addAttachment("SQL Запрос:", sql);
        Connection connection;
        try {
            connection = getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, entity.getName());
                stmt.setString(2, entity.getJob());
                stmt.setLong(3, entity.getId());

                int rowsAffected = stmt.executeUpdate();
                connection.commit();

                if (rowsAffected > 0) {
                    logger.info("Успешно обновлен пользователь с ID: {}", entity.getId());
                    return findById(entity.getId());
                } else {
                    logger.warn("Не найден пользователь с ID: {}", entity.getId().toString());
                    return null;
                }
            }
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Ошибка при откате транзакции: {}", rollbackEx.getMessage());
            }
            logger.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            throw new RuntimeException("Не удалось обновить пользователя", e);
        }
    }

    /**
     * Удаляет пользователя из базы данных по имени и должности.
     *
     * @param entity пользователь для удаления
     */
    @Override
    @Step("Удалить пользователя из базы данных")
    public void delete(User entity) {
        String sql = "DELETE FROM " + tableName + " WHERE name = ? AND job = ?";
        logSql(sql, entity.getName(), entity.getJob());
        Allure.addAttachment("SQL Запрос:", sql);
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getJob());
            int rowsAffected = stmt.executeUpdate();
            getConnection().commit();
            logger.info("Удалено {} пользователей с именем: {} и должностью: {}",
                    String.valueOf(rowsAffected), entity.getName(), entity.getJob());
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Ошибка при откате транзакции: {}", rollbackEx.getMessage());
            }
            logger.error("Не удалось удалить пользователя: {}", e.getMessage());
            throw new RuntimeException("Не удалось удалить пользователя", e);
        }
    }

    /**
     * Удаляет пользователя из базы данных по идентификатору.
     *
     * @param id идентификатор пользователя для удаления
     */
    @Override
    @Step("Удалить пользователя из базы данных")
    public void deleteById(Long id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        logSql(sql, id);
        Allure.addAttachment("SQL Запрос:", sql);
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            getConnection().commit();
            logger.info("Удалено {} пользователей с ID: {}", String.valueOf(rowsAffected), String.valueOf(id));
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Ошибка при откате транзакции: {}", rollbackEx.getMessage());
            }
            logger.error("Не удалось удалить пользователя: {}", e.getMessage());
            throw new RuntimeException("Не удалось удалить пользователя", e);
        }
    }

    /**
     * Получает список всех пользователей из базы данных.
     *
     * @return список всех пользователей
     */
    @Override
    @Step("Получить список пользователей из базы данных")
    public List<User> findAll() {
        String sql = "SELECT * FROM " + tableName;
        Allure.addAttachment("SQL Запрос:", sql);
        logSql(sql);
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(User.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .job(rs.getString("job"))
                        .build());
            }
            return users;
        } catch (SQLException e) {
            logger.error("Не удалось получить список пользователей: {}", e.getMessage());
            throw new RuntimeException("Не удалось получить список пользователей", e);
        }
    }

    /**
     * Создает таблицу пользователей, если она не существует.
     */
    @Override
    @Step("Создать таблицу USERS в базе данных")
    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + "id BIGSERIAL PRIMARY KEY,"
                + "name VARCHAR(255),"
                + "job VARCHAR(255)"
                + ")";
        logSql(sql);
        Allure.addAttachment("SQL Запрос:", sql);
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.executeUpdate();
            getConnection().commit();
            logger.info("Успешно создана таблица {}", tableName);

        } catch (SQLException e) {
            logger.error("Не удалось создать таблицу: {}", e.getMessage());
            throw new RuntimeException("Не удалось создать таблицу", e);
        }
    }

    /**
     * Удаляет всех пользователей из таблицы.
     */
    @Override
    @Step("Очистить таблицу USERS")
    public void deleteAll() {
        String sql = "TRUNCATE TABLE " + tableName + " RESTART IDENTITY";
        logSql(sql);
        Allure.addAttachment("SQL Запрос:", sql);
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.executeUpdate();
            getConnection().commit();
            logger.info("Успешно очищена таблица {}", tableName);
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Ошибка при откате транзакции: {}", rollbackEx.getMessage());
            }
            logger.error("Не удалось очистить таблицу: {}", e.getMessage());
            throw new RuntimeException("Не удалось очистить таблицу", e);
        }
    }
}
