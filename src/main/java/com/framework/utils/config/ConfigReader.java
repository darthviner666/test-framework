package com.framework.utils.config;

import org.aeonbits.owner.ConfigFactory;

/**
 * Класс для чтения свойств из конфига.
 */
public class ConfigReader {
    /** Единственный экземпляр конфигурации проекта */
    private static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class, System.getProperties());

    /**
     * Возвращает экземпляр конфигурации проекта.
     * Реализует паттерн Singleton для доступа к конфигурации.
     *
     * @return экземпляр ProjectConfig
     */
    public static ProjectConfig Instance() {
        return config;
    }
}
