package com.framework.api.serialize;

import io.qameta.allure.Allure;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Класс для сериализации объектов в json.
 */
public class JsonSerializer {
    private static final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL); // игнорировать null

    /**
     * Сериализовать объект в json.
     *
     * @param object - объект.
     * @return - json String.
     */
    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            Allure.addAttachment("FAIL", "Failed to serialize object");
            throw new RuntimeException("Failed to serialize object", e);
        }
    }
}
