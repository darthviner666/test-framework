package com.db;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.asserts.AssertionsWithAllureLog;
import com.framework.database.JdbcActions;
import com.framework.utils.dataGenerators.CreateUserGenerator;
import com.testBase.TestBase;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

public class JdbcCreateUserTest extends TestBase {
    @Test(description = "Проверка создания и получения пользователя в базе данных PostgreSQL",
            groups = "smoke",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void createUserPostgresJdbcTest() {
        CreateUserPojoRq user = CreateUserGenerator.generateUser();
        JdbcActions.createUser(user);
        CreateUserPojoRq createdUser = JdbcActions.retriveUser(user);
        AssertionsWithAllureLog.assertEquals(createdUser.job, user.job, "Поле job");
        AssertionsWithAllureLog.assertEquals(createdUser.name, user.name, "Поле name");
    }
}
