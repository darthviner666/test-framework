package com.framework.api.helpers;

import com.framework.api.ApiRequests;
import com.framework.api.Endpoints;
import com.framework.api.HeadersBuilder;
import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.api.pojo.users.create.rs.CreateUserPojoRs;
import com.framework.api.pojo.users.get.rs.GetUserPojoRs;
import com.framework.asserts.AssertionsWithAllureLog;
import com.framework.utils.deserialize.JsonDeserializer;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Действия с контроллером user.
 */
public class UserHelper {

    /**
     * Создать пользователя.
     * @param user - пользователь.
     * @return - созданый пользователь.
     */
    public CreateUserPojoRs createUser(CreateUserPojoRq user) {
        Map<String, String> headers = HeadersBuilder
                .defaultHeaders()
                .withHeader("x-api-key","reqres-free-v1")
                .build();

        Response response = ApiRequests
                .sendRequest("Создать пользователя",
                        Endpoints.CREATE_USER,
                        Method.POST,
                        req -> req.headers(headers).body(user));

        AssertionsWithAllureLog.assertEquals(response.getStatusCode(),201, "Статус код запроса");

        return JsonDeserializer.deserialize(response
                .getBody().asString(), CreateUserPojoRs.class);
    }

    /**
     * Получить пользователей.
     * @param page - страница.
     * @return - массив пользователей.
     */
    public GetUserPojoRs[] getUsers(Integer page) {
        Map<String, String> queryParams = new HashMap<>() {{
            put("page", page.toString());
        }};

        Map<String, String> headers = HeadersBuilder
                .defaultHeaders()
                .withHeader("x-api-key","reqres-free-v1")
                .build();

        Response response = ApiRequests
                .sendRequest("Получить пользователей",
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
//TODO доделать десериализацию через путь
        return JsonDeserializer.deserialize(response.getBody().asString(),"data", GetUserPojoRs[].class);
    }


}
