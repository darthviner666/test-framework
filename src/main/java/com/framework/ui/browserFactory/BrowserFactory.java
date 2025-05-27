package com.framework.ui.browserFactory;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.framework.utils.config.ProjectConfig;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Optional;

import static com.framework.utils.config.ConfigReader.Instance;

/**
 * Класс для настройки браузеров.
 */
public class BrowserFactory {
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
        Configuration.headless = config.headless();

    }

    /**
     * Настройка локального браузера.
     * @param config - конфиг-ридер.
     * @param browser - браузер.
     * @param browserVersion - версия браузера.
     */
    private static void setupLocalBrowser(ProjectConfig config ,String browser, String browserVersion) {
        Configuration.browser = browser;
        Configuration.browserVersion = browserVersion;
        Configuration.headless = config.headless();
    }


    /**
     * Настройка удалённого браузера.
     * @param config - конфиг-ридер.
     * @param browser - браузер.
     * @param browserVersion - версия браузера.
     */
    private static void setupRemoteBrowser(ProjectConfig config, String browser, String browserVersion) {
        Configuration.remote = config.remoteUrl();
        Configuration.browser = browser;
        Configuration.browserVersion = browserVersion;

        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("enableVNC", config.enableVnc());
        capabilities.setCapability("enableVideo", config.enableVideo());
        Configuration.browserCapabilities = capabilities;
    }

    /**
     * Настройка Selenoid.
     * @param config - конфиг-ридер.
     * @param browser - браузер.
     * @param browserVersion - версия браузера.
     */
    private static void setupSelenoid(ProjectConfig config, String browser, String browserVersion) {
        // Конфигурация Selenoid
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser);
        capabilities.setVersion(browserVersion);
        capabilities.setCapability("enableVNC", config.enableVnc());
        capabilities.setCapability("enableVideo", config.enableVideo());
        capabilities.setCapability("screenResolution", config.browserSize());

        // Конфигурация Selenide
        Configuration.remote = config.remoteUrl();
        Configuration.browserCapabilities = capabilities;
        Configuration.browserSize = config.browserSize();
        Configuration.timeout = config.timeout();
    }

    /**
     * Очистить куки.
     */
    public static void clearCookies() {
        WebDriverRunner.clearBrowserCache();
        WebDriverRunner.getWebDriver().manage().deleteAllCookies();
    }
}
