package com.framework.database;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.utils.config.ProjectConfig;
import com.framework.utils.logger.TestLogger;

import java.sql.*;

import static com.framework.utils.config.ConfigReader.Instance;

public class JdbcActions  {
    private static final ProjectConfig config = Instance();
    private static final TestLogger logger = new TestLogger(JdbcActions.class);

    public static Connection connectToDatabase() {
        logger.info("Подключение к базе данных");
        String url = config.databaseUrl();
        String user = config.databaseUser();
        String password = config.databasePassword();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            logger.info("База данных подключена успешно!");
            return conn;
        } catch (SQLException e) {
            logger.error("Ошибка подключения к базе данных: " + e.getMessage());
        }
        return null;
    }

    public static void createUser(CreateUserPojoRq user) {
        try (Connection conn = connectToDatabase()) {
            String sql = "INSERT INTO create_users (name, job) VALUES (?, ?)";
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, user.name);
                    stmt.setString(2, user.job);
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка при создании пользователя: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static CreateUserPojoRq retriveUser(CreateUserPojoRq user) {
        try (Connection conn = connectToDatabase()) {
            String sql = "SELECT * FROM create_users WHERE name = ? AND job = ?";
            if (conn != null) {
                logger.info("База данных подключена успешно!");
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    if (stmt != null) {
                        stmt.setString(1, user.name);
                        stmt.setString(2, user.job);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                return CreateUserPojoRq.builder()
                                        .id(rs.getLong("id"))
                                        .name(rs.getString("name"))
                                        .job(rs.getString("job"))
                                        .build();
                            }
                            return null;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка при создании пользователя: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

}