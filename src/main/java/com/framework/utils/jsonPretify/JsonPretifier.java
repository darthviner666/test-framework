package com.framework.utils.jsonPretify;

import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.SerializationFeature;
import lombok.experimental.UtilityClass;

/**
 * Класс для обработки json.
 */
public final class JsonPretifier {
    /**
     * Экземпляр ObjectMapper для работы с json.
     * Используется для форматирования и обработки json строк.
     */
    private static final ObjectMapper mapper = new ObjectMapper();
    /**
     * Логгер для логирования событий в классе JsonPretifier.
     * Используется для записи информации о форматировании json.
     */
    private static final TestLogger log = new TestLogger(JsonPretifier.class);
    /**
     * Статический блок инициализации.
     * Настройка ObjectMapper для форматирования json.
     */
    static {
        // Настройка ObjectMapper для форматирования json
        log.info("Initializing ObjectMapper for JSON pretification");
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Включает форматирование с отступами
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//Игнорирует поля со значением null
        log.info("ObjectMapper initialized successfully");
    }
    /**
     * Приватный конструктор класса JsonPretifier.
     * Предотвращает создание экземпляров класса.
     */
    private JsonPretifier() {
        // Utility class
    }
    /**
     * Метод для форматирования тела запроса/ответа в читаемый вид.
     * Если форматирование не удалось, возвращает исходное тело.
     *
     * @param body тело запроса или ответа
     * @return отформатированное тело или исходное, если форматирование не удалось
     */
    public static String prettifyOrRaw(String body) {
        if (body == null || body.isEmpty()) return "";
        try {
            return pretifyJson(body);
        } catch (Exception e) {
            Allure.addAttachment("WARN: prettify failed", e.getMessage());
            return body;
        }
    }
    /**
     * Форматировать строку в читаемый json.
     * @param json - входная строка.
     * @return - форматированый json.
     */
    public static String pretifyJson(String json) {
        try {
            log.info("Prettifying JSON string");
            Object jsonObject = mapper.readValue(json, Object.class);
            return mapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            log.error("Error while prettifying JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while prettifying JSON", e);
        }
    }
}
