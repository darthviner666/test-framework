package com.framework.database.jdbc.users;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.database.jdbc.UserJdbcRepository;
import io.qameta.allure.Step;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUsersActions extends UserJdbcRepository {

    private static final String TABLE_NAME = "Users";

    public JdbcUsersActions() throws SQLException {
        super();
    }

    @Step("Создать пользователя в таблице users")
    public static void createUser(CreateUserPojoRq user) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (name, job) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.name);
            stmt.setString(2, user.job);
            stmt.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error during rollback: {}", rollbackEx.getMessage());
            }
            logger.error("Failed to create user: {}", e.getMessage());
            throw e;
        }
    }

    @Step("Получить пользователя из таблицы users")
    public static CreateUserPojoRq getUser(String name) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return CreateUserPojoRq.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .job(rs.getString("job"))
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve user: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    private void deleteUser(String name) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {

            logger.error("Failed to delete user: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Обновляет данные пользователя в базе данных
     *
     * @param id          ID пользователя для обновления
     * @param updatedUser объект с новыми данными пользователя
     * @return true если обновление успешно, false если пользователь не найден
     */
    @Step("Обновить данные пользователя в таблице users")
    public boolean updateUser(Long id, CreateUserPojoRq updatedUser) {
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, job = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, updatedUser.name);
            stmt.setString(2, updatedUser.job);
            stmt.setLong(3, id);

            int rowsAffected = stmt.executeUpdate();
            connection.commit();

            if (rowsAffected > 0) {
                logger.info("Successfully updated user with ID: {}", id);
                return true;
            } else {
                logger.warn("No user found with ID: {}", id.toString());
                return false;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error during rollback: {}", rollbackEx.getMessage());
            }
            logger.error("Error updating user: {}", e.getMessage());
            throw new RuntimeException("Failed to update user", e);
        }
    }
    /**
     * Очищает таблицу пользователей
     * @return количество удаленных записей
     */
    @Step("Очистить таблицу users")
    public int clearTable() {
        String sql = "DELETE FROM " + TABLE_NAME;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            logger.info("Successfully cleared users table. Removed {} records", rowsAffected);
            return rowsAffected;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error during rollback: {}", rollbackEx.getMessage());
            }
            logger.error("Error clearing users table: {}", e.getMessage());
            throw new RuntimeException("Failed to clear users table", e);
        }
    }

    /**
     * Альтернативный метод очистки таблицы с использованием TRUNCATE
     */
    @Step("Очистить таблицу users")
    public void truncateTable() {
        String sql = "TRUNCATE TABLE " + TABLE_NAME + " RESTART IDENTITY";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            connection.commit();
            logger.info("Successfully truncated users table");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error during rollback: {}", rollbackEx.getMessage());
            }
            logger.error("Error truncating users table: {}", e.getMessage());
            throw new RuntimeException("Failed to truncate users table", e);
        }
    }
}
