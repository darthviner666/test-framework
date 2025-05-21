package com.framework.api.deserialize;

import io.qameta.allure.Allure;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import lombok.experimental.UtilityClass;

/**
 * Класс для десериализации JSON
 */
@UtilityClass
public class JsonDeserializer {

    /**
     * Десериализовать json в объект.
     * @param json - json.
     * @param clazz - целевой класс.
     * @param path - путь в json.
     * @return - объект.
     * @param <T> - целевой тип.
     */
    public static  <T> T deserialize(String json, String path, Class<T> clazz) {
        try {
           return JsonPath.from(json).getObject(path,clazz);
        } catch (Exception e) {
            Allure.addAttachment("FAIL","Failed to deserialize JSON to " + clazz.getSimpleName());
            throw new RuntimeException("Failed to deserialize JSON to " + clazz.getSimpleName(), e);
        }
    }
    /**
     * Десериализовать json в объект.
     * @param json - json.
     * @param clazz - целевой класс.
     * @return - объект.
     * @param <T> - целевой тип.
     */
    public static <T> T deserialize(String json, Class<T> clazz) {
       return deserialize(json,"",clazz);
    }
}
