package com.framework.database.jdbc.users;

import com.framework.database.jdbc.JdbcConnectManager;
import com.framework.database.jdbc.repositories.Repository;
import com.framework.database.tables.User;
import com.framework.utils.logger.TestLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryJdbc extends Repository<User> {
    private static final TestLogger logger = new TestLogger(UserRepositoryJdbc.class);
    private final JdbcConnectManager jdbcManager;

    public UserRepositoryJdbc() throws SQLException {
        this.jdbcManager = JdbcConnectManager.getInstance();
        this.tableName = "users";
    }

    private Connection getConnection() throws SQLException {
        return jdbcManager.getConnection();
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
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

    @Override
    public User save(User entity) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (name, job) VALUES (?, ?)";
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

    @Override
    public User update(User entity) {
        String sql = "UPDATE " + tableName + " SET name = ?, job = ? WHERE id = ?";
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

    @Override
    public void delete(User entity) {
        String sql = "DELETE FROM " + tableName + " WHERE name = ? AND job = ?";
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

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
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

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM " + tableName;
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

    @Override
    public void deleteAll() {
        String sql = "TRUNCATE TABLE " + tableName + " RESTART IDENTITY";
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
