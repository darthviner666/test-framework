package com.framework.api.filters;

import com.framework.utils.logger.TestLogger;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
/**
 * Кастомный фильтр RestAssured для логирования запросов и ответов.
 * Логирует HTTP метод, URI, заголовки и тело запроса, а также статус ответа, заголовки и тело ответа.
 */

public class CustomRestAssuredFilter implements Filter {
    /**
     * Логгер для логирования событий.
     * Используется для записи информации о запросах и ответах.
     */
    private static final TestLogger log = new TestLogger(CustomRestAssuredFilter.class);

    /**
     * Метод фильтра, который выполняет логирование запросов и ответов.
     *
     * @param requestSpec  спецификация запроса
     * @param responseSpec спецификация ответа
     * @param ctx          контекст фильтрации
     * @return ответ после выполнения запроса
     */
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        log.info("API REQUEST: {} {}", requestSpec.getMethod(), requestSpec.getURI());
        if (requestSpec.getHeaders() != null && !requestSpec.getHeaders().asList().isEmpty()) {
            log.info("Headers: {}", requestSpec.getHeaders());
        }
        if (requestSpec.getBody() != null) {
            log.info("Body: {}", requestSpec.getBody());
        }

        Response response = ctx.next(requestSpec, responseSpec);

        log.info("API RESPONSE: {} {}", String.valueOf(response.getStatusCode()), response.getStatusLine());
        if (response.getHeaders() != null && !response.getHeaders().asList().isEmpty()) {
            log.info("Response Headers: {}", response.getHeaders());
        }
        if (response.getBody() != null) {
            log.info("Response Body: {}", response.getBody().asPrettyString());
        }
        return response;
    }
}

