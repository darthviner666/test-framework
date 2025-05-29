package com.framework.utils.config;

import org.aeonbits.owner.ConfigFactory;

/**
 * Класс для чтения свойств из конфига.
 */
public class ConfigReader {
    private static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class, System.getProperties());

    public static ProjectConfig Instance() {
        return config;
    }
}
