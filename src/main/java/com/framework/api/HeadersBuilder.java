package com.framework.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для работы с заголовками.
 */
public class Headers {
    private final Map<String, String> headers = new HashMap<>();

    /**
     * Стандартные заголовки.
     * @return - Стандартные заголовки.
     */
    public static Headers defaultHeaders() {
        return new Headers()
                .withContentTypeJson()
                .withAcceptJson();
    }

    /**
     * Заголовок "Content-Type".
     * @return - application/json.
     */
    public Headers withContentTypeJson() {
        headers.put("Content-Type", "application/json");
        return this;
    }


    /**
     * Заголовок "Accept".
     * @return - application/json.
     */
    public Headers withAcceptJson() {
        headers.put("Accept", "application/json");
        return this;
    }

    /**
     * Заголовок "Authorization".
     * @return - token.
     */
    public Headers withAuthorization(String token) {
        headers.put("Authorization", "Bearer " + token);
        return this;
    }

    /**
     * Кастомнй заголовок.
     * @param name - имя заголовка.
     * @param value - значение заголовка.
     * @return - заголовок со значением.
     */
    public Headers withHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    /**
     * Вернуть заголовки.
     * @return - заголовки.
     */
    public Map<String, String> build() {
        return new HashMap<>(headers);
    }
}
