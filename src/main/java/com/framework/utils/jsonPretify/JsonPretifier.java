package com.framework.utils.jsonPretify;

import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

/**
 * Класс для обработки json.
 */
@UtilityClass
public class JsonPretifier {
    /**
     * Форматировать строку в читаемый json.
     * @param json - входная строка.
     * @return - форматированый json.
     */
   public String pretifyJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object jsonObject = mapper.readValue(json, Object.class);
            return mapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while minifying JSON", e);
        }

    }
}
