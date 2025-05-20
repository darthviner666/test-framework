package com.framework.api;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import java.util.function.Function;

import static io.restassured.RestAssured.given;
@UtilityClass
public class ApiRequests {
    public Response sendRequest(
            String requestDescription,
            String endpoint,
            Method httpMethod,
            Function<RequestSpecification, RequestSpecification> requestBuilder,
            Function<Response, Response> responseValidator) {

        return Allure.step(requestDescription, () -> {
            RequestSpecification requestSpec = requestBuilder.apply(given());
            Response response = requestSpec.request(httpMethod);
            return responseValidator != null ? responseValidator.apply(response) : response;
        });
    }

    public Response sendRequest(
            String requestDescription,
            String endpoint,
            Method httpMethod,
            Function<RequestSpecification, RequestSpecification> requestBuilder
    ) {
        return sendRequest(
                requestDescription,
                endpoint,
                httpMethod,
                requestBuilder,
                null);
    }

    public Response sendRequest(
            String requestDescription,
            String endpoint,
            Method httpMethod){
        return sendRequest(requestDescription,
                endpoint,
                httpMethod,
                req -> req,
                null);
    }
}

