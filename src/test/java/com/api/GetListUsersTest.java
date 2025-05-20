package com.api;

import com.TestBase;
import com.framework.api.ApiRequests;
import com.framework.api.Endpoints;
import com.framework.api.HeadersBuilder;
import com.framework.utils.Asserts;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

//TODO разобраться с аллюром
@Feature("Get Users List")
public class GetListUsersTest extends TestBase {
    @Test
    @DisplayName("Get Users List Test OK")
    @Story("Положительный сценарий")
    public void getUsersListOkTest() {

        Map<String,String> queryParams = new HashMap<>() {{
            put("page","2");
        }};

        Map<String,String> headers = HeadersBuilder
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

        //response.statusCode();

        //Asserts.assertEquals(200,response.getStatusCode(),"статус код");
    }
}
