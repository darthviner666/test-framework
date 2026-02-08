package com.framework.ui.browserFactory;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.WebDriverRunner;
import com.framework.utils.config.ProjectConfig;
import com.framework.utils.logger.TestLogger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.Optional;

import java.util.HashMap;
import java.util.Map;

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
            setupSelenoidBrowser(config, actualBrowser, actualVersion);
        } else {
            // local, ci, или любой другой режим - локальный Selenide
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

        // Selenoid options в W3C-формате (selenoid:options) — убирает deprecation warning
        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", config.enableVnc());
        selenoidOptions.put("enableVideo", config.enableVideo());
        selenoidOptions.put("screenResolution", config.browserSize());
        selenoidOptions.put("timeZone", "Europe/Moscow");

        MutableCapabilities capabilities = createBrowserOptions(browser, browserVersion);
        capabilities.setCapability("selenoid:options", selenoidOptions);

        Configuration.browserCapabilities = capabilities;
        Configuration.headless = false;
        // Дополнительные настройки для удаленного запуска
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        Configuration.remoteReadTimeout = 300000;
        Configuration.remoteConnectionTimeout = 300000;
        Configuration.baseUrl = config.baseUrl();

        log.info("Selenoid браузер настроен: {} {}, URL: {}",
                browser, browserVersion, config.remoteUrl());
    }

    private static MutableCapabilities createBrowserOptions(String browser, String browserVersion) {
        if ("chrome".equalsIgnoreCase(browser)) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--remote-allow-origins=*", "--disable-gpu");
            if (browserVersion != null && !browserVersion.isEmpty()) {
                options.setBrowserVersion(browserVersion);
            }
            return options;
        } else if ("firefox".equalsIgnoreCase(browser)) {
            FirefoxOptions options = new FirefoxOptions();
            if (browserVersion != null && !browserVersion.isEmpty()) {
                options.setBrowserVersion(browserVersion);
            }
            return options;
        } else if ("edge".equalsIgnoreCase(browser)) {
            EdgeOptions options = new EdgeOptions();
            if (browserVersion != null && !browserVersion.isEmpty()) {
                options.setBrowserVersion(browserVersion);
            }
            return options;
        } else {
            return new ChromeOptions();
        }
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