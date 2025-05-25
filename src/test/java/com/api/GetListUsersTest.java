package com.api;

import com.TestBase;
import com.framework.api.helpers.UserHelper;
import com.framework.api.pojo.users.get.rs.GetUserPojoRs;
import com.framework.asserts.AssertionsWithAllureLog;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("API Тесты")
@Feature("Работа с пользователями")
@Severity(SeverityLevel.BLOCKER)
public class GetListUsersTest extends TestBase {
    @DataProvider(name = "data", parallel = true)
    public Integer[][] provideData() {
        return new Integer[][]{
                {1},
                {2}
        };
    }

    @Test(description = "Проверка получения пользователей на странице", testName = "Получить пользователей", dataProvider = "data", threadPoolSize = 2)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void getUsersListOkTest(Integer page) {

        String testName = String.format("Аутентификация пользователей на странице: %s", page.toString());
        Allure.getLifecycle().updateTestCase(test -> test.setName(testName));

        UserHelper helper = new UserHelper();

        logStep("Получить пользователей");
        GetUserPojoRs[] users = helper.getUsers(page);

        logStep("Сравнить поля");
        for (GetUserPojoRs user : users) {
            AssertionsWithAllureLog.assertNotEquals(user.firstName, "", "Имя не пустое");
            AssertionsWithAllureLog.assertNotEquals(user.lastName, "", "Фамилия не пустая");
            String email = user.firstName.toLowerCase() + "." + user.lastName.toLowerCase();
            AssertionsWithAllureLog.assertTrue(user.email.startsWith(email), "Email");
        }
    }
}
