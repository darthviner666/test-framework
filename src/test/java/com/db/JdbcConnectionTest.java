package com.db;

import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Тест подключения к базе данных PostgreSQL.
 */
public class JdbcTestConnection {
    @Test
    public void connectionTest() {
        String url = "jdbc:postgresql://interchange.proxy.rlwy.net:46439/railway";
        String user = "postgres";
        String password = "ENeseoxlEoeXdxAWSaMOEOjvFWrhTtjw";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected successfully!");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
