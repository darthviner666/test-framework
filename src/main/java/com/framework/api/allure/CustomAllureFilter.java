package com.framework.api.allure;

import com.framework.utils.jsonPretify.JsonPretifier;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.Date;

/**
 * Класс для настройки логирования API запросов в Allure.
 */
//TODO добавить разделение на функции/классы
    //TODO pretify json response body
public class CustomAllureFilter implements OrderedFilter {
    private final AllureRestAssured allureFilter = new AllureRestAssured();

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        // 1. Добавляем дополнительную информацию в Allure перед запросом
        Allure.addAttachment("Request timestamp", "text/plain", new Date().toString());

        // 2. Добавляем пользовательские заголовки в отчет
        if (requestSpec.getHeaders() != null) {
            StringBuilder headers = new StringBuilder();
            requestSpec.getHeaders().forEach(header ->
                    headers.append(header.getName()).append(": ").append(header.getValue()).append("\n"));
            Allure.addAttachment("Request Headers", "text/plain", headers.toString());
        }

        // 3. Стандартная обработка AllureRestAssured

        // Логирование тела запроса
        if (requestSpec.getBody() != null) {
            String requestBody = requestSpec.getBody().toString();
            Allure.addAttachment("Request Body", "application/json", JsonPretifier.pretifyJson(requestBody));
        }
        Response response = allureFilter.filter(requestSpec, responseSpec, ctx);


        // 4. Добавляем дополнительную информацию после получения ответа
        Allure.addAttachment("Response time (ms)", "text/plain",
                String.valueOf(response.getTime()));

        // Логирование тела ответа
        if (response.getBody() != null) {
            Allure.addAttachment("Response Body", "application/json", JsonPretifier.pretifyJson(response.getBody().asString()));

        }
        return response;
    }

        @Override
        public int getOrder () {
            return OrderedFilter.LOWEST_PRECEDENCE; // Фильтр выполняется последним
        }
    }
