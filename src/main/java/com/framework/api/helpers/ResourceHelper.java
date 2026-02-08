package com.framework.api.helpers;

import com.framework.api.endpoints.Endpoints;
import com.framework.api.pojo.resource.Resource;
import com.framework.api.restAssured.ApiRequests;
import com.framework.api.restAssured.ApiSpecs;
import com.framework.utils.deserialize.JsonDeserializer;
import com.framework.utils.logger.TestLogger;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

/**
 * Действия с контроллером /api/products
 */
public class ResourceHelper {
    /**
     * Логгер для логирования действий.
     */
    protected TestLogger LOGGER = new TestLogger(UserHelper.class);

    /**
     * Получить ресурсы.
     *
     * @param page - страница.
     * @return - массив пользователей.
     */
    public Resource[] getResources(Integer page, File jsonShema) {
        LOGGER.info("Получение ресурсы, страница: {}", page);
        Map<String, String> queryParams = new HashMap<>() {{
            put("page", page.toString());
        }};

        Response response = ApiRequests
                .sendRequest("Получить Ресурсы",
                        Endpoints.RESOURCES.toString(),
                        Method.GET,
                        ApiSpecs
                                .getRequestSpecWithApiKey()
                                .queryParams(queryParams),
                        new ResponseSpecBuilder()
                                .addResponseSpecification(ApiSpecs.getDefaultResponseSpec())
                                .expectStatusCode(200)
                                .expectBody(JsonSchemaValidator.matchesJsonSchema(jsonShema))
                                .expectBody("page", equalTo(page))
                                .expectBody("per_page", equalTo(6))
                                .expectBody("total", equalTo(12))
                                .expectBody("total_pages", equalTo(2))
                                .expectBody("data.size()", equalTo(6))
                                .build()
                );

        LOGGER.info("Ресурсы успешно получены, количество: {}", response.jsonPath().getList("data").size());
        return JsonDeserializer.deserialize(response.getBody().asString(), "data", Resource[].class);
    }

}
