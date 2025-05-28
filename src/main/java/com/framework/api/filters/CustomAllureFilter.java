package com.framework.api.filters;

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
public class CustomAllureFilter implements OrderedFilter {
    /**
     * Логгер для логирования событий.
     * Используется для записи информации о запросах и ответах.
     */
    private final AllureRestAssured allureFilter = new AllureRestAssured();

    /**
     * Метод для форматирования тела запроса/ответа в читаемый вид.
     * Если форматирование не удалось, возвращает исходное тело.
     *
     * @param body тело запроса или ответа
     * @return отформатированное тело или исходное, если форматирование не удалось
     */
    private String prettifyOrRaw(String body) {
        if (body == null || body.isEmpty()) return "";
        try {
            return JsonPretifier.pretifyJson(body);
        } catch (Exception e) {
            Allure.addAttachment("WARN: prettify failed", e.getMessage());
            return body;
        }
    }

    /**
     * Метод фильтра, который выполняет логирование запросов и ответов в Allure.
     *
     * @param requestSpec  спецификация запроса
     * @param responseSpec спецификация ответа
     * @param ctx          контекст фильтрации
     * @return ответ после выполнения запроса
     */
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
            String prettyBody = prettifyOrRaw(requestBody);
            String contentType = requestSpec.getContentType() != null ? requestSpec.getContentType() : "text/plain";
            Allure.addAttachment("Request Body", contentType, prettyBody);
        }
        Response response = allureFilter.filter(requestSpec, responseSpec, ctx);


        // 4. Добавляем дополнительную информацию после получения ответа
        Allure.addAttachment("Response time (ms)", "text/plain",
                String.valueOf(response.getTime()));

        // Логирование тела ответа
        if (response.getBody() != null) {
            String responseBody = response.getBody().asString();
            String prettyBody = prettifyOrRaw(responseBody);
            String contentType = response.getContentType() != null ? response.getContentType() : "text/plain";
            Allure.addAttachment("Response Body", contentType, prettyBody);
        }
        return response;
    }

    /**
     * Возвращает порядок выполнения фильтра.
     * Фильтр выполняется последним, чтобы другие фильтры могли добавить свои данные в Allure.
     *
     * @return порядок выполнения фильтра
     */
    @Override
    public int getOrder() {
        return OrderedFilter.LOWEST_PRECEDENCE; // Фильтр выполняется последним
    }
}
