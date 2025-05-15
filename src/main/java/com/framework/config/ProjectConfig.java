package com.framework.config;
import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:config.properties",
        "system:properties"
})
public interface ProjectConfig extends Config {

    @Config.Key("browser")
    String browser();

    @Config.Key("browser.version")
    String browserVersion();

    @Config.Key("browser.size")
    String browserSize();

    @Config.Key("base.url")
    String baseUrl();

    @Config.Key("api.base.url")
    String apiBaseUrl();

    @Config.Key("headless")
    boolean headless();

    @Config.Key("timeout")
    long timeout();
}

