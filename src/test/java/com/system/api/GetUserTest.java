package com.system.api;

import com.framework.api.helpers.UserHelper;
import com.framework.api.pojo.users.get.rs.GetUserPojoRs;
import com.testBase.TestBase;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("API Тесты")
@Feature("Работа с пользователями")
@Severity(SeverityLevel.BLOCKER)
public class GetUserTest extends TestBase {

    @Test(description = "Проверка получения пользователей на странице",
            testName = "Получить пользователей",
            groups = "smoke",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void getUserTest() {
        UserHelper userHelper = new UserHelper();
        GetUserPojoRs user = userHelper.getUser(1);
        System.out.println();

    }
}
