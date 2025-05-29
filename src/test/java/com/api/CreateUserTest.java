package com.api;

import com.testBase.TestBase;
import com.framework.api.helpers.UserHelper;
import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.api.pojo.users.create.rs.CreateUserPojoRs;
import com.framework.asserts.AssertionsWithAllureLog;
import com.framework.utils.dataGenerators.CreateUserGenerator;
import com.framework.utils.serialize.JsonSerializer;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Класс для тестирования создания пользователей через API.
 */
@Epic("API Тесты")
@Feature("Работа с пользователями")
@Severity(SeverityLevel.BLOCKER)
public class CreateUserTest extends TestBase {

    /**
     * Дата провайдер для генерации данных пользователей.
     * @return массив сгенерированных пользователей.
     */
    @DataProvider(name = "data", parallel = true)
    public CreateUserPojoRq[][] provideData() {
        return new CreateUserPojoRq[][]{
                {CreateUserGenerator.generateUser()},
                {CreateUserGenerator.generateUser()}
        };
    }

    /**
     * Тест для проверки создания пользователей.
     * @param user объект пользователя, который будет создан.
     */
    @Test(description = "Проверка создания пользователей",
            testName = "Cоздание пользователей",
            dataProvider = "data",
            groups = "smoke",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void createUserTest(CreateUserPojoRq user) {
        String testName = String.format("Создание пользователя: %s", JsonSerializer.toJson(user));
        Allure.getLifecycle().updateTestCase(test -> test.setName(testName));

        UserHelper helper = new UserHelper();

        CreateUserPojoRs userRs = helper.createUser(user);

        AssertionsWithAllureLog.assertEquals(userRs.job, user.job, "Поле job");
        AssertionsWithAllureLog.assertEquals(userRs.name, user.name, "Поле name");
    }

}


