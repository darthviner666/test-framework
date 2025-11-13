package com.system.api;

import com.framework.api.endpoints.Endpoints;
import com.framework.api.restAssured.ApiSpecs;
import com.testBase.TestBase;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("API Тесты")
@Feature("Работа с рессурсами")
@Severity(SeverityLevel.CRITICAL)
public class GetListResourceWithoutPojo extends TestBase {


    @Test(description = "Проверка получения рессурсов на странице",
            testName = "Получить рессурсы",
            groups = "smoke",
            priority = 1)
    @Story("Положительный сценарий")
    public void getListResourceWithoutPojo() {
        given()
                .spec(ApiSpecs.getDefaultRequestSpec())
                .get(Endpoints.RESOURCE.toString())
                .then()
                .spec(ApiSpecs.getDefaultResponseSpec())
                .statusCode(200)
                .and()
                .body("data[0].id",equalTo(1))
                .body("data[0].name", equalTo("cerulean"))
                .body("data[1].id", equalTo(2));
               // .body("data.size()",  equalTo(6));

    }
}
