package com.system.api;

import com.framework.api.endpoints.Endpoints;
import com.framework.api.restAssured.ApiSpecs;
import com.framework.utils.config.ConfigReader;
import com.testBase.TestBase;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Epic("API Тесты")
@Feature("Работа с пользователями")
@Severity(SeverityLevel.BLOCKER)
public class GetUserNotFoundTest extends TestBase {

    @DataProvider(name = "data")
    public Integer[][] dataProvider() {
        return new Integer[][] {
                {23},
                {24}
        };
    }


    @Test(description = "Проверка что запрос неуществующего пользователя возвращаеет 404",
            testName = "Запрос несуществующего пользователя",
            dataProvider = "data",
            groups = "smoke",
            priority = 1)
    @Story("Негативный сценарий")
    public void getUserNotFoundTest(Integer id) {
        given()
                .spec(ApiSpecs.getDefaultRequestSpec())
                .headers("x-api-key","reqres-free-v1")
                .pathParam("id",id)
                .get(Endpoints.USER.toString())
                .then()
                .statusCode(404);
    }
}
