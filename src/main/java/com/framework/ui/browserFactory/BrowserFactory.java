package com.framework.ui.browserFactory;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
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
     * Сброс конфигурации для избежания конфликтов
     */
    private static void resetConfiguration() {
        Configuration.browser = "";
        Configuration.browserVersion = "";
        Configuration.remote = null;
        Configuration.browserCapabilities = null;
        Configuration.headless = false;

        System.clearProperty("webdriver.chrome.driver");
        System.clearProperty("webdriver.gecko.driver");
        System.clearProperty("webdriver.edge.driver");
        System.clearProperty("browser");
        System.clearProperty("browserVersion");
    }

    private static String determineRunMode(ProjectConfig config) {
        // Проверяем переменные окружения Jenkins
        if (System.getenv("CI") != null && System.getenv("CI").equalsIgnoreCase("true")) {
            return "selenoid";
        }
        if (System.getenv("JENKINS_HOME") != null) {
            return "selenoid";
        }
        if (System.getProperty("run.mode") != null) {
            return System.getProperty("run.mode");
        }
        return config.runMode().toLowerCase();
    }

    public static void setupBrowser(@Optional String browser, String browserVersion) {
        resetConfiguration();

        ProjectConfig config = Instance();
        String runMode = determineRunMode(config); // Используем новую логику

        switch (runMode) {
            case "remote":
            case "selenoid":
                setupSelenoid(config, browser, browserVersion);
                break;
            default:
                setupLocalBrowser(config, browser, browserVersion);
        }


    Configuration.reportsFolder = "target/allure-results";
    Configuration.browserSize = config.browserSize();
    Configuration.timeout = config.timeout();
    Configuration.baseUrl = config.baseUrl();
    Configuration.pageLoadStrategy = "eager";
    Configuration.pageLoadTimeout = 20000;
        log.info("Настройки браузера установлены. Режим: {}, CI: {}", runMode);
    }

    private static boolean isCiEnvironment() {
        String ci = System.getenv("CI");
        String jenkinsHome = System.getenv("JENKINS_HOME");
        String runMode = System.getProperty("run.mode", "");

        return "true".equalsIgnoreCase(ci) ||
                jenkinsHome != null ||
                "ci".equalsIgnoreCase(runMode) ||
                "jenkins".equalsIgnoreCase(runMode);
    }

//    /**
//     * Настройка браузеров.
//     * @param browser - браузер.
//     * @param browserVersion - версия браузера.
//     */
//    public static void setupBrowser(@Optional String browser, String browserVersion) {
//        resetConfiguration();
//
//        ProjectConfig config = Instance();
//        Configuration.webdriverLogsEnabled = true;
//
//        // Автоматически определяем режим в CI
//        String runMode = config.runMode().toLowerCase();
//        boolean isCi = isCiEnvironment();
//
//        if (isCi) {
//            log.info("Обнаружено CI окружение, принудительно используем Selenoid");
//            setupSelenoid(config, browser, browserVersion);
//        } else { }
//
//    }

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
        log.info("Установка Selenoid браузера: {} {}", browser, browserVersion);

        // Создаем DesiredCapabilities
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser);
        capabilities.setVersion(browserVersion);
        capabilities.setCapability("enableVNC", config.enableVnc());
        capabilities.setCapability("enableVideo", config.enableVideo());
        capabilities.setCapability("screenResolution", config.browserSize());

        // Добавляем другие важные параметры для Selenoid
        capabilities.setCapability("sessionTimeout", "5m");
        capabilities.setCapability("timeZone", "Europe/Moscow");
        capabilities.setCapability("env", new String[]{"LANG=ru_RU.UTF-8", "LANGUAGE=ru:en", "LC_ALL=ru_RU.UTF-8"});

        // Для Chrome дополнительные опции
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--verbose");
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }

        // Конфигурация Selenide
        log.info("Настройка Selenide для Selenoid, remote URL: {}", config.remoteUrl());
        Configuration.remote = config.remoteUrl();
        Configuration.browserCapabilities = capabilities;
        Configuration.browserSize = config.browserSize();
        Configuration.timeout = config.timeout();

        // Важно для Selenoid
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        Configuration.remoteReadTimeout = 300000; // 5 минут

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
