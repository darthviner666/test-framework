package com.framework.api.helpers;

import com.framework.api.restAssured.ApiRequests;
import com.framework.api.endpoints.Endpoints;
import com.framework.api.restAssured.ApiSpecs;
import com.framework.api.restAssured.HeadersBuilder;
import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.api.pojo.users.create.rs.CreateUserPojoRs;
import com.framework.api.pojo.users.get.rs.GetUserPojoRs;
import com.framework.asserts.AssertionsWithLog;
import com.framework.utils.deserialize.JsonDeserializer;
import com.framework.utils.logger.TestLogger;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Действия с контроллером user.
 */
public class UserHelper {

    /**
     * Логгер для логирования действий.
     */
    protected TestLogger logger = new TestLogger(UserHelper.class);
    /**
     * Создать пользователя.
     * @param user - пользователь.
     * @return - созданый пользователь.
     */
    public CreateUserPojoRs createUser(CreateUserPojoRq user) {
        logger.info("Создание пользователя: {}", user);

        Response response = ApiRequests
                .sendRequest("Создать пользователя",
                        Endpoints.USERS.toString(),
                        Method.POST,
                        req -> req.spec(ApiSpecs.getDefaultRequestSpec()).body(user));

        AssertionsWithLog.assertEquals(response.getStatusCode(),201, "Статус код запроса");

        logger.info("Пользователь успешно создан: {}");

        return JsonDeserializer.deserialize(response
                .getBody().asString(), CreateUserPojoRs.class);
    }

    /**
     * Получить пользователей.
     * @param page - страница.
     * @return - массив пользователей.
     */
    public GetUserPojoRs[] getUsers(Integer page) {
        logger.info("Получение пользователей, страница: {}", page);
        Map<String, String> queryParams = new HashMap<>() {{
            put("page", page.toString());
        }};

        Response response = ApiRequests
                .sendRequest("Получить пользователей",
                        Endpoints.USERS.toString(),
                        Method.GET,
                        req -> req.spec(ApiSpecs
                                .getDefaultRequestSpec()
                                .queryParams(queryParams))

                );

        AssertionsWithLog
                .assertEquals(200,
                        response.getStatusCode(),
                        "статус код");
        logger.info("Пользователи успешно получены, количество: {}", response.jsonPath().getList("data").size());
        return JsonDeserializer.deserialize(response.getBody().asString(),"data", GetUserPojoRs[].class);
    }

    /**
     * Получить пользователя.
     * @param id - id пользователя.
     * @return - пользователь.
     */
    public GetUserPojoRs getUser(int id) {
        Response response = ApiRequests
                .sendRequest("Получить пользователя",
                        Endpoints.USER.toString(),
                        Method.GET,
                        req -> ApiSpecs
                                .getDefaultRequestSpec()
                                .pathParam("id",id));
        logger.info("Пользователь успешно получен");
        return response.jsonPath().getObject("data", GetUserPojoRs.class);
    }
}
