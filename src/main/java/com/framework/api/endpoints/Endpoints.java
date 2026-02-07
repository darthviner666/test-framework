package com.framework.api.endpoints;

/**
 * Класс с эндпоинтами.
 */
public enum Endpoints {
    /**
     * Получить пользователей.
     */
    USERS("/api/users"),

    USER("/api/users/{id}"),

    RESOURCE("/api/unknown"),

    RESOURCES("/api/products");

    private String url;

    Endpoints(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
