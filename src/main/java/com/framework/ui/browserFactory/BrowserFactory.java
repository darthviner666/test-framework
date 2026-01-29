package com.framework.ui.browserFactory;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.WebDriverRunner;
import com.framework.utils.config.ProjectConfig;
import com.framework.utils.logger.TestLogger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.Optional;

import static com.framework.utils.config.ConfigReader.Instance;

public class BrowserFactory {
    private static final TestLogger log = new TestLogger(BrowserFactory.class);

    /**
     * Сброс конфигурации для избежания конфликтов
     */
    private static void resetConfiguration() {
        Configuration.browser = "";
        Configuration.browserVersion = "";
        Configuration.remote = null;
        Configuration.browserCapabilities = null;
        Configuration.headless = false;

        // Очищаем только системные свойства драйверов
        System.clearProperty("webdriver.chrome.driver");
        System.clearProperty("webdriver.gecko.driver");
        System.clearProperty("webdriver.edge.driver");
    }

    /**
     * Настройка браузеров.
     */
    public static void setupBrowser(@Optional String browser, @Optional String browserVersion) {
        resetConfiguration();

        browser = "chrome";

        ProjectConfig config = Instance();

        // Если браузер не передан, берем из конфига
        String actualBrowser = (browser == null || browser.isEmpty()) ?
                config.browser() : browser;
        String actualVersion = (browserVersion == null || browserVersion.isEmpty()) ?
                "" : browserVersion;

        log.info("Настройка браузера: {}, версия: {}, режим: {}",
                actualBrowser, actualVersion, config.runMode());

        Configuration.webdriverLogsEnabled = true;
        Configuration.reportsFolder = "target/allure-results";
        Configuration.browserSize = config.browserSize();
        Configuration.timeout = config.timeout();
        Configuration.baseUrl = config.baseUrl();
        Configuration.pageLoadStrategy = "eager";
        Configuration.pageLoadTimeout = 20000;

        // Определяем режим запуска
        String runMode = config.runMode().toLowerCase();

        if ("remote".equals(runMode) || "selenoid".equals(runMode)) {
            setupRemoteBrowser(config, actualBrowser, actualVersion);
        } else {
            setupLocalBrowser(config, actualBrowser, actualVersion);
        }

        log.info("Настройки браузера установлены. Mode: {}, Browser: {}",
                runMode, actualBrowser);
    }

    /**
     * Настройка локального браузера.
     */
    private static void setupLocalBrowser(ProjectConfig config, String browser, String browserVersion) {
        log.info("Установка локального браузера: {} {}", browser, browserVersion);

        Configuration.browser = browser;
        Configuration.browserVersion = browserVersion;
        Configuration.headless = config.headless();

        if ("chrome".equalsIgnoreCase(browser)) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");

            if (config.headless()) {
                options.addArguments("--headless=new");
            }

            Configuration.browserCapabilities = options;
        }

        log.info("Локальный браузер настроен: {} {}", browser, browserVersion);
    }

    /**
     * Настройка удаленного браузера (Selenoid/Selenium Grid).
     */
    private static void setupRemoteBrowser(ProjectConfig config, String browser, String browserVersion) {
        log.info("Установка удаленного браузера: {} {}", browser, browserVersion);

        // Для Selenoid важно установить тип браузера
        Configuration.browser = browser;

        if (browserVersion != null && !browserVersion.isEmpty()) {
            Configuration.browserVersion = browserVersion;
        }

        Configuration.remote = config.remoteUrl();

        // Создаем capabilities
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", browser);

        if (browserVersion != null && !browserVersion.isEmpty()) {
            capabilities.setCapability("browserVersion", browserVersion);
        }

        // Selenoid capabilities
        capabilities.setCapability("enableVNC", config.enableVnc());
        capabilities.setCapability("enableVideo", config.enableVideo());
        capabilities.setCapability("screenResolution", config.browserSize());
        capabilities.setCapability("timeZone", "Europe/Moscow");

        // Браузер-специфичные опции
        if ("chrome".equalsIgnoreCase(browser)) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--remote-allow-origins=*");
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        } else if ("firefox".equalsIgnoreCase(browser)) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
        } else if ("edge".equalsIgnoreCase(browser)) {
            EdgeOptions edgeOptions = new EdgeOptions();
            capabilities.setCapability("ms:edgeOptions", edgeOptions);
        }

        Configuration.browserCapabilities = capabilities;

        // Настройки для удаленного запуска
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        Configuration.remoteReadTimeout = 300000;
        Configuration.remoteConnectionTimeout = 300000;

        log.info("Удаленный браузер настроен: {} {}, URL: {}",
                browser, browserVersion, config.remoteUrl());
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