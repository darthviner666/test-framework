package com.api;

import com.TestBase;
import com.asserts.AssertionsWithAllureLog;
import com.framework.api.ApiRequests;
import com.framework.api.Endpoints;
import com.framework.api.HeadersBuilder;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("API Тесты")
@Feature("Работа с пользователями")
public class GetListUsersTest extends TestBase {
    @Test(description = "Получить пользователей")
    @Story("Положительный сценарий")
    public void getUsersListOkTest() {

        Map<String, String> queryParams = new HashMap<>() {{
            put("page", "2");
        }};

        Map<String, String> headers = HeadersBuilder
                .defaultHeaders()
                .build();

        ApiRequests
                .sendRequest("получить пользователей",
                        Endpoints.GET_USERS,
                        Method.GET,
                        req -> req
                                .headers(headers)
                                .queryParams(queryParams),
                        res -> res
                                .then()
                                .statusCode(200)
                                .extract().response()
                );
        Response response = ApiRequests.sendRequest("получить пользователей",
                Endpoints.GET_USERS,
                Method.GET);
//        Response response = ApiRequests.sendGetRequest(
//                "получить пользователей",
//                Endpoints.GET_USERS);

        response.statusCode();

        AssertionsWithAllureLog.assertEquals(200,response.getStatusCode(),"статус код");
    }
}
