package com.integration.db.jdbc;

import com.framework.asserts.AssertionsWithLog;
import com.framework.database.jdbc.users.UserRepositoryJdbc;
import com.framework.database.tables.User;
import com.framework.utils.dataGenerators.user.UserGenerator;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;

@Epic("Интеграционные тесты")
@Feature("Работа с базой данных Postgres SQL")
@Severity(SeverityLevel.BLOCKER)
public class JdbcDbUserTest extends JdbcTestBase {

    private UserRepositoryJdbc userRepositoryJdbc;
    private User user;

    @BeforeMethod
    public void setUp() {
        try {
            user = new UserGenerator().generate();

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
        try {
            user = userRepositoryJdbc.save(user);
            User foundUser = userRepositoryJdbc.findById(user.getId());
            AssertionsWithLog.assertNotNull(user);
            AssertionsWithLog.assertEquals(user.getName(), foundUser.getName(),"Поле: Имя");
            AssertionsWithLog.assertEquals(user.getJob(), foundUser.getJob(),"Поле: Работа");
            userRepositoryJdbc.delete(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test(description = "Проверка изменения пользователя в базе данных PostgreSQL",
            groups = "integration",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void updateUserPostgresJdbcTest() {
        try {
            user = userRepositoryJdbc.save(user);
            userRepositoryJdbc.update(user);
            User foundUser = userRepositoryJdbc.findById(user.getId());
            AssertionsWithLog.assertNotNull(user);
            AssertionsWithLog.assertEquals(user.getName(), foundUser.getName(),"Поле: Имя");
            AssertionsWithLog.assertEquals(user.getJob(), foundUser.getJob(),"Поле: Работа");
            userRepositoryJdbc.delete(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test(description = "Проверка очистки таблицы USERS в базе данных PostgreSQL",
            groups = "integration",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void deleteTableUserPostgresJdbcTest() {
        try {
            user = userRepositoryJdbc.save(user);
            userRepositoryJdbc.deleteAll();
            List<User> foundUsers = userRepositoryJdbc.findAll();
            AssertionsWithLog.assertTrue(foundUsers.isEmpty(),"Таблица не очищена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
