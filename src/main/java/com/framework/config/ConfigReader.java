package com.framework.config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigReader {
    private static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class, System.getProperties());

    public static ProjectConfig Instance() {
        return config;
    }
}
