package com.system.api;

import com.framework.utils.logger.TestLogger;
import com.testBase.TestBase;
import com.framework.api.helpers.UserHelper;
import com.framework.api.pojo.users.get.rs.GetUserPojoRs;
import com.framework.asserts.AssertionsWithLog;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("API Тесты")
@Feature("Работа с пользователями")
@Severity(SeverityLevel.BLOCKER)
public class GetListUsersTest extends TestBase {
    private final static TestLogger LOGGER = new TestLogger(GetListUsersTest.class);

    @DataProvider(name = "data", parallel = true)
    public Integer[][] provideData() {
        return new Integer[][]{
                {1},
                {2}
        };
    }

    @Test(description = "Проверка получения пользователей на странице",
            testName = "Получить пользователей",
            dataProvider = "data",
            groups = "smoke",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void getUsersListOkTest(Integer page) {
        LOGGER.info("Получение пользователей, страница: {}", page);
        Map<String, String> queryParams = new HashMap<>() {{
            put("page", page.toString());
        }};

        UserHelper helper = new UserHelper();

        GetUserPojoRs[] users = helper.getUsers(page);

        for (GetUserPojoRs user : users) {
            AssertionsWithLog.assertNotEquals(user.firstName, "", "Имя не пустое");
            AssertionsWithLog.assertNotEquals(user.lastName, "", "Фамилия не пустая");
            String email = user.firstName.toLowerCase() + "." + user.lastName.toLowerCase();
            AssertionsWithLog.assertTrue(user.email.startsWith(email), "Email");
        }
    }
}
