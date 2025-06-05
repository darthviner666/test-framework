package com.framework.utils.config;

import org.aeonbits.owner.Config;

/**
 * Свойства из конфига с помощью библиотеки Owner.
 */
@Config.Sources({
        "classpath:config.properties",
        "system:properties"
})
public interface ProjectConfig extends Config {

    @Config.Key("browser")
    String browser();

    @Config.Key("base.url")
    String baseUrl();

    @Config.Key("api.base.url")
    String apiBaseUrl();

    @Config.Key("headless")
    boolean headless();

    @Config.Key("timeout")
    long timeout();

    @Key("browser.name")
    @DefaultValue("chrome")
    String browserName();

    @Key("browser.version")
    String browserVersion();

    @Key("run.mode")
    @DefaultValue("local")
    String runMode(); // local / remote / grid

    @Key("remote.url")
    @DefaultValue("http://localhost:4444/wd/hub")
    String remoteUrl();

    @Key("browser.size")
    @DefaultValue("1920x1080")
    String browserSize();

    @Key("enable.vnc")
    @DefaultValue("false")
    boolean enableVnc();

    @Key("enable.video")
    @DefaultValue("false")
    boolean enableVideo();

    @Key("database.url")
    String databaseUrl();

    @Key("database.username")
    String databaseUser();

    @Key("database.password")
    String databasePassword();

}

