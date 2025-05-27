package com.framework.api.restAssured;

import com.framework.api.filters.CustomAllureFilter;
import com.framework.api.filters.CustomRestAssuredFilter;
import com.framework.utils.config.ConfigReader;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import java.util.function.Function;

import static io.restassured.RestAssured.given;

/**
 * Класс для отправки API запросов.
 */
@UtilityClass
public class ApiRequests {

    /**
     * Отправка API запроса.
     * @param requestDescription - описание запроса.
     * @param endpoint - эндпоинт.
     * @param httpMethod - HTTP метод.
     * @param requestBuilder - параметры запроса.
     * @return - ответ.
     */
    @Step("API: {httpMethod} {endpoint} : {requestDescription}")
    public Response sendRequest(
            String requestDescription,
            String endpoint,
            Method httpMethod,
            Function<RequestSpecification, RequestSpecification> requestBuilder) {

        RequestSpecification requestSpec = requestBuilder.apply(given());
        requestSpec
                .baseUri(ConfigReader.Instance().apiBaseUrl())
                .filter(new CustomAllureFilter())
                .filter(new CustomRestAssuredFilter());
        return requestSpec.request(httpMethod, endpoint)
                .then()
                .extract().response();
    }

    /**
     * Отправить дефолтный запрос без тела с дефолтными хэдерами.
     * @param requestDescription - описание запроса.
     * @param endpoint - эндпоинт.
     * @param httpMethod - HTTP метод.
     * @return - ответ.
     */
    public Response sendRequest(
            String requestDescription,
            String endpoint,
            Method httpMethod) {
        return sendRequest(
                requestDescription,
                endpoint,
                httpMethod,
                req -> req
                        .headers(HeadersBuilder.defaultHeaders().build()));
    }
    /**
     * Отправить дефолтный запрос c телом с дефолтными хэдерами.
     * @param requestDescription - описание запроса.
     * @param endpoint - эндпоинт.
     * @param httpMethod - HTTP метод.
     * @param body - тело запроса.
     * @return - ответ.
     */
    public Response sendRequest(
            String requestDescription,
            String endpoint,
            Method httpMethod,
            Object body) {
        return sendRequest(requestDescription,
                endpoint,
                httpMethod,
                req -> req
                        .body(body)
                        .headers(HeadersBuilder.defaultHeaders().build()));
    }

}

