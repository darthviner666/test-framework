package com.framework.utils.serialize;

import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * Класс для сериализации объектов в json.
 */
public final class JsonSerializer {
    /**
     * Экземпляр ObjectMapper для работы с json.
     * Используется для сериализации объектов в json строки.
     */
    private static final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL) // игнорировать null
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // игнорировать пустые объекты

    /**
     * Экземпляр логгера для тестов.
     */
    private static final TestLogger log = new TestLogger(JsonSerializer.class);
    static {
        // Настройка ObjectMapper для форматирования json
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Включает форматирование с отступами
    }
    /**
     * Приватный конструктор класса JsonSerializer.
     * Предотвращает создание экземпляров класса.
     */
    private JsonSerializer() {
        // Utility class
    }

    /**
     * Сериализовать объект в json.
     *
     * @param object - объект.
     * @return - json String.
     */
    public static String toJson(Object object) {
        if (object == null) {
            log.warn("Attempted to serialize null object");
            Allure.addAttachment("WARN", "Attempted to serialize null object");
            return null;
        }
        try {
            log.info("Serializing object: {}", String.valueOf(object));
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            Allure.addAttachment("FAIL", "Failed to serialize object: " + String.valueOf(object));
            log.error("Error while serializing object: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to serialize object: " + String.valueOf(object), e);
        }
    }
}
