package com.api;

import com.TestBase;
import com.asserts.AssertionsWithAllureLog;
import com.framework.api.ApiRequests;
import com.framework.api.Endpoints;
import com.framework.api.HeadersBuilder;
import io.qameta.allure.*;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("API Тесты")
@Feature("Работа с пользователями")
@Severity(SeverityLevel.BLOCKER)
public class GetListUsersTest extends TestBase {
    @DataProvider(name = "data")
    public Integer[][] provideData() {
        return new Integer[][] {
                {1},
                {2}
        };
    }

    @Test(description = "Проверка получения пользователей на странице {page}",testName = "Получить пользователей", dataProvider = "data", threadPoolSize = 2)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void getUsersListOkTest(Integer page) {

        Map<String, String> queryParams = new HashMap<>() {{
            put("page", page.toString());
        }};

        Map<String, String> headers = HeadersBuilder
                .defaultHeaders()
                .withAcceptJson()
                .withContentTypeJson()
                .withHeader("x-api-key","reqres-free-v1")
                .build();

        Response response = ApiRequests
                .sendRequest("получить пользователей",
                        Endpoints.GET_USERS,
                        Method.GET,
                        req -> req
                                .headers(headers)
                                .queryParams(queryParams)
                );

        AssertionsWithAllureLog
                .assertEquals(200,
                        response.getStatusCode(),
                        "статус код");
    }
}
