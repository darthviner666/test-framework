package com.framework.ui.browserFactory;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.WebDriverRunner;
import com.framework.utils.config.ProjectConfig;
import com.framework.utils.logger.TestLogger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Optional;

import static com.framework.utils.config.ConfigReader.Instance;

public class BrowserFactory {
    private static final TestLogger log = new TestLogger(BrowserFactory.class);

    /**
     * Сброс конфигурации для избежания конфликтов
     */
    private static void resetConfiguration() {
        Configuration.browser = "chrome"; // Устанавливаем значение по умолчанию
        Configuration.browserVersion = "";
        Configuration.remote = null;
        Configuration.browserCapabilities = null;
        Configuration.headless = false;
    }

    /**
     * Настройка браузеров.
     */
    public static void setupBrowser(@Optional String browser, @Optional String browserVersion) {
        resetConfiguration();

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
            // Для Selenoid используем специальную конфигурацию
            setupSelenoidBrowser(config, actualBrowser, actualVersion);
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
     * Настройка браузера для Selenoid.
     * ВАЖНО: Для Selenoid используется специальный подход
     */
    private static void setupSelenoidBrowser(ProjectConfig config, String browser, String browserVersion) {
        log.info("Установка браузера для Selenoid: {} {}", browser, browserVersion);

        // Для Selenoid ОБЯЗАТЕЛЬНО устанавливаем remote URL
        Configuration.remote = config.remoteUrl();

        // И browser тоже устанавливаем
        Configuration.browser = browser;

        if (browserVersion != null && !browserVersion.isEmpty()) {
            Configuration.browserVersion = browserVersion;
        }

        // Создаем DesiredCapabilities для Selenoid
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser);

        if (browserVersion != null && !browserVersion.isEmpty()) {
            capabilities.setVersion(browserVersion);
        }

        // Selenoid capabilities
        capabilities.setCapability("enableVNC", config.enableVnc());
        capabilities.setCapability("enableVideo", config.enableVideo());
        capabilities.setCapability("screenResolution", config.browserSize());
        capabilities.setCapability("timeZone", "Europe/Moscow");

        // Для Chrome добавляем ChromeOptions
        if ("chrome".equalsIgnoreCase(browser)) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--remote-allow-origins=*");
            chromeOptions.addArguments("--disable-gpu");


            // Добавляем ChromeOptions в capabilities
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }

        // Устанавливаем capabilities
        Configuration.browserCapabilities = capabilities;
        Configuration.headless = false;
        // Дополнительные настройки для удаленного запуска
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        Configuration.remoteReadTimeout = 300000;
        Configuration.remoteConnectionTimeout = 300000;

        log.info("Selenoid браузер настроен: {} {}, URL: {}",
                browser, browserVersion, config.remoteUrl());
    }

    /**
     * Очистить куки.
     */
    public static void clearCookies() {
        log.info("Очистка куков");
        WebDriverRunner.clearBrowserCache();
        if (WebDriverRunner.hasWebDriverStarted()) {
            WebDriverRunner.getWebDriver().manage().deleteAllCookies();
        }
        log.info("Куки очищены");
    }
}