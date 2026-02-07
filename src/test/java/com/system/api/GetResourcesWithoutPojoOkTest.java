package com.system.api;

import com.framework.api.endpoints.Endpoints;
import com.framework.api.restAssured.ApiSpecs;
import com.framework.utils.logger.TestLogger;
import com.testBase.TestBase;
import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("API Тесты")
@Feature("Работа с ресурсами")
@Severity(SeverityLevel.BLOCKER)
public class GetResourcesWithoutPojoOkTest extends TestBase {

    private final TestLogger LOGGER = new TestLogger(GetResourcesWithoutPojoOkTest.class);

    @DataProvider(name = "data", parallel = true)
    public Integer[][] provideData() {
        return new Integer[][]{
                {1},
                {2}
        };
    }

    @Test(description = "Проверка получения ресурсов",
            testName = "Получить ресурсы",
            groups = "smoke",
            dataProvider = "data",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void getResourcesOkTest(Integer page) {
        LOGGER.info("Получение пользователей, страница: {}", page);
        Map<String, String> queryParams = new HashMap<>() {{
            put("page", page.toString());
        }};

        File jsonShema = new File("src/test/resources/schemas/resourceSchema.json");

        given()
                .spec(ApiSpecs.getRequestSpecWithApiKey())
                .queryParams(queryParams)
                .get(Endpoints.RESOURCES.toString())
                .then()
                .spec(ApiSpecs.getDefaultResponseSpec())
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchema(jsonShema))
                .body("page", equalTo(page))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .body("data.size()", equalTo(6));


    }
}
