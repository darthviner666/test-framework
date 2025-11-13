package com.framework.api.filters;

import com.framework.utils.jsonPretify.JsonPretifier;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
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
        Response response = allureFilter.filter(requestSpec, responseSpec, ctx);
        Allure.addAttachment("Response time","text/plain", String.format("Response time (ms): %s \n", response.getTime()));
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
