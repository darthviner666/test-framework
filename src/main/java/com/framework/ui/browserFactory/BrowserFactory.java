package com.framework.ui.browserFactory;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.framework.utils.config.ProjectConfig;
import com.framework.utils.logger.TestLogger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Optional;

import static com.framework.utils.config.ConfigReader.Instance;

/**
 * Класс для настройки браузеров.
 */
public class BrowserFactory {
    /**
     * Логгер для логирования событий браузера.
     * Используется для записи информации о настройке браузеров.
     */
    private static final TestLogger log = new TestLogger(BrowserFactory.class);
    /**
     * Настройка браузеров.
     * @param browser - браузер.
     * @param browserVersion - версия браузера.
     */
    public static void setupBrowser(@Optional String browser, String browserVersion) {
        ProjectConfig config = Instance();
        Configuration.webdriverLogsEnabled = true;

        switch (config.runMode().toLowerCase()) {
            case "remote":
                setupRemoteBrowser(config, browser, browserVersion);
                break;
            case "selenoid":
                setupSelenoid(config, browser, browserVersion);
            default:
                setupLocalBrowser(config ,browser, browserVersion);
        }
        Configuration.reportsFolder = "target/allure-results";
        Configuration.browserSize = config.browserSize();
        Configuration.timeout = config.timeout();
        Configuration.baseUrl = config.baseUrl();
        Configuration.pageLoadStrategy = "eager";
        Configuration.pageLoadTimeout = 20000;
        log.info("Настройки браузера установлены");

    }

    /**
     * Настройка локального браузера.
     * @param config - конфиг-ридер.
     * @param browser - браузер.
     * @param browserVersion - версия браузера.
     */
    private static void setupLocalBrowser(ProjectConfig config ,String browser, String browserVersion) {
        log.info("Установка локального браузера: {} {}", browser, browserVersion);
        Configuration.browser = browser;
        Configuration.browserVersion = browserVersion;
        Configuration.headless = config.headless();
       if (browser.equals("chrome")) {

           Configuration.browserCapabilities = new ChromeOptions()
                   .addArguments("--disable-gpu")
                   .addArguments("--no-sandbox")
                   .addArguments("--disable-dev-shm-usage");

       }
        log.info("Локальный браузер настроен: {} {}", browser, browserVersion);
    }


    /**
     * Настройка удалённого браузера.
     * @param config - конфиг-ридер.
     * @param browser - браузер.
     * @param browserVersion - версия браузера.
     */
    private static void setupRemoteBrowser(ProjectConfig config, String browser, String browserVersion) {
        log.info("Установка удалённого браузера: {} {}", browser, browserVersion);
        Configuration.remote = config.remoteUrl();
        Configuration.browser = browser;
        Configuration.browserVersion = browserVersion;

        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("enableVNC", config.enableVnc());
        capabilities.setCapability("enableVideo", config.enableVideo());
        Configuration.browserCapabilities = capabilities;
        log.info("Удалённый браузер настроен: {} {}", browser, browserVersion);
    }

    /**
     * Настройка Selenoid.
     * @param config - конфиг-ридер.
     * @param browser - браузер.
     * @param browserVersion - версия браузера.
     */
    private static void setupSelenoid(ProjectConfig config, String browser, String browserVersion) {
        // Конфигурация Selenoid
        log.info("Установка Selenoid браузера: {} {}", browser, browserVersion);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser);
        capabilities.setVersion(browserVersion);
        capabilities.setCapability("enableVNC", config.enableVnc());
        capabilities.setCapability("enableVideo", config.enableVideo());
        capabilities.setCapability("screenResolution", config.browserSize());
        log.info("Selenoid браузер настроен: {} {}", browser, browserVersion);

        // Конфигурация Selenide
        log.info("Настройка Selenide для Selenoid");
        Configuration.remote = config.remoteUrl();
        Configuration.browserCapabilities = capabilities;
        Configuration.browserSize = config.browserSize();
        Configuration.timeout = config.timeout();
        log.info("Настройка Selenide для Selenoid завершена");
    }

    /**
     * Очистить куки.
     */
    public static void clearCookies() {
        log.info("Очистка куков");
        WebDriverRunner.clearBrowserCache();
        WebDriverRunner.getWebDriver().manage().deleteAllCookies();
        log.info("Куки очищены");
    }
}
