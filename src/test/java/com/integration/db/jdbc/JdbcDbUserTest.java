package com.integration.db.jdbc;

import com.framework.asserts.AssertionsWithAllureLog;
import com.framework.database.jdbc.users.UserRepositoryJdbc;
import com.framework.database.tables.User;
import com.framework.utils.dataGenerators.user.UserGenerator;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class JdbcDbUserTest extends JdbcTestBase {

    private UserRepositoryJdbc userRepositoryJdbc;

    @BeforeMethod
    public void setUp() {
        try {
            userRepositoryJdbc = new UserRepositoryJdbc();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Test(description = "Проверка создания и получения пользователя в базе данных PostgreSQL",
            groups = "integration",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void createUserPostgresJdbcTest() {
        User user = new UserGenerator().generate();
        try {
            user = userRepositoryJdbc.save(user);
            User foundUser = userRepositoryJdbc.findById(user.getId());
            AssertionsWithAllureLog.assertNotNull(user);
            AssertionsWithAllureLog.assertEquals(user.getName(), foundUser.getName(),"Поле: Имя");
            AssertionsWithAllureLog.assertEquals(user.getJob(), foundUser.getJob(),"Поле: Работа");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
