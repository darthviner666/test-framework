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

    /**
     * Возвращает название браузера из конфигурации.
     * 
     * @return название браузера
     */
    @Config.Key("browser")
    String browser();

    /**
     * Возвращает базовый URL приложения.
     * 
     * @return базовый URL
     */
    @Config.Key("base.url")
    String baseUrl();

    /**
     * Возвращает базовый URL API.
     * 
     * @return базовый URL API
     */
    @Config.Key("api.base.url")
    String apiBaseUrl();

    /**
     * Определяет, запускать ли браузер в режиме без графического интерфейса.
     * 
     * @return true, если браузер должен запускаться в headless режиме
     */
    @Config.Key("headless")
    boolean headless();

    /**
     * Возвращает значение таймаута для ожидания элементов.
     * 
     * @return таймаут в миллисекундах
     */
    @Config.Key("timeout")
    long timeout();

    /**
     * Возвращает название браузера с дефолтным значением "chrome".
     * 
     * @return название браузера
     */
    @Key("browser.name")
    @DefaultValue("chrome")
    String browserName();

    /**
     * Возвращает версию браузера.
     * 
     * @return версия браузера
     */
    @Key("browser.version")
    String browserVersion();

    /**
     * Возвращает режим запуска тестов (local, remote, grid).
     * 
     * @return режим запуска
     */
    @Key("run.mode")
    @DefaultValue("local")
    String runMode(); // local / remote / grid

    /**
     * Возвращает URL удаленного сервера для запуска тестов.
     * 
     * @return URL удаленного сервера
     */
    @Key("remote.url")
    @DefaultValue("http://localhost:4444/wd/hub")
    String remoteUrl();

    /**
     * Возвращает размер окна браузера.
     * 
     * @return размер окна в формате "ширинаxвысота"
     */
    @Key("browser.size")
    @DefaultValue("1920x1080")
    String browserSize();

    /**
     * Определяет, включать ли VNC для удаленного просмотра.
     * 
     * @return true, если VNC должен быть включен
     */
    @Key("enable.vnc")
    @DefaultValue("false")
    boolean enableVnc();

    /**
     * Определяет, включать ли запись видео при выполнении тестов.
     * 
     * @return true, если запись видео должна быть включена
     */
    @Key("enable.video")
    @DefaultValue("false")
    boolean enableVideo();

    /**
     * Возвращает URL для подключения к базе данных.
     * 
     * @return URL базы данных
     */
    @Key("database.url")
    String databaseUrl();
    /**
     * Возвращает имя пользователя для подключения к базе данных.
     * 
     * @return имя пользователя базы данных
     */
    @Key("database.username")
    String databaseUser();

    /**
     * Возвращает пароль для подключения к базе данных.
     * 
     * @return пароль для базы данных
     */
    @Key("database.password")
    String databasePassword();

}

