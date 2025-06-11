package com.integration.db.jdbc;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

/**
 * Тест подключения к базе данных PostgreSQL.
 */
public class JdbcConnectionTest extends JdbcTestBase {

@Test(description = "Проверка подключения к базе данных PostgreSQL",
            groups = "smoke",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void connectionTest() {
    }
}
