package com.framework.api.restAssured;

import com.framework.api.filters.CustomAllureFilter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.Map;

import static com.framework.utils.config.ConfigReader.Instance;
import static org.hamcrest.Matchers.lessThan;

/**
 * Класс для получения спецификаций запросов и ответов REST ASSURED.
 */
public class ApiSpecs {

    /**
     * Читаем из properties BASE_URL.
     */
    private static final String BASE_URL = Instance().apiBaseUrl();
    private static final CustomAllureFilter CUSTOM_ALLURE_FILTER = new CustomAllureFilter();
    /**
     * Получить стандартные спецификации запроса.
     * @return - спецификации запроса.
     */
    public static RequestSpecification getDefaultRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(CUSTOM_ALLURE_FILTER)
                .log(LogDetail.ALL)
                .build().given();
    }

    /**
     * Получить стандартные спецификации ответа.
     * @return - спецификации ответа.
     */
    public static ResponseSpecification getDefaultResponseSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(10000L))
                .log(LogDetail.ALL)
                .build();
    }


    /**
     * Получить стандартные спецификации запроса с авторизацией через токен.
     * @return - спецификации запроса.
     */
    public static RequestSpecification getRequestSpecWithAuth(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getDefaultRequestSpec())
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

    /**
     * Получить спеки с заголовками.
     * @param headers - заголовки.
     * @return - спецификации запроса.
     */
    public static RequestSpecification getRequestSpecWithHeaders(Map<String,String> headers) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getDefaultRequestSpec())
                .addHeaders(headers)
                .build();
    }

    /**
     * Получить спеки с параметрами пути.
     * @param pathParams - параметры пути.
     * @return - спецификации запроса.
     */
    public static RequestSpecification getRequestSpecWithPathParams(Map<String, ?> pathParams) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getDefaultRequestSpec())
                .addPathParams(pathParams)
                .build();
    }

    /**
     * Получить спеки с параметрами запроса.
     * @param queryParams - параметры запроса.
     * @return - спецификации запроса.
     */
    public static RequestSpecification getRequestSpecWithQueryParams(Map<String, ?> queryParams) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getDefaultRequestSpec())
                .addQueryParams(queryParams)
                .build();
    }


}
