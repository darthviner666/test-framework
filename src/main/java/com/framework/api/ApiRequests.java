package com.framework.api;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.function.Function;

import static io.restassured.RestAssured.given;

public class ApiRequests {

    @Step("API: {method} {endpoint} {requestDescription}")
    public static Response sendRequest(
            String requestDescription,
            Method method,
            String endpoint,
            Function<Response, Response> responseValidator,
            Function<RequestSpecification, RequestSpecification> requestBuilder) {

        return Allure.step(requestDescription, () -> {
            // 1. Строим запрос
            RequestSpecification requestSpec = requestBuilder.apply(given());

            // 2. Отправляем запрос
            Response response = requestSpec.when().request(method,endpoint);

            // 3. Валидируем ответ (если валидатор предоставлен)
            if (responseValidator != null) {
                return responseValidator.apply(response);
            }

            return response;
        });
    }

    // Перегруженный метод без валидации
    public static Response sendRequest(
            String requestDescription,
            Method method,
            Function<RequestSpecification, RequestSpecification> requestBuilder, String endpoint) {

        return sendRequest(requestDescription, method, endpoint, null, requestBuilder);
    }

}

