package com.db.jdbc;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

public class JdbcCreateUserTest extends JdbcTestBase {
    @Test(description = "Проверка создания и получения пользователя в базе данных PostgreSQL",
            groups = "smoke",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void createUserPostgresJdbcTest() {
    }
}
