package com;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.framework.api.ApiSpecs;
import com.framework.config.ConfigReader;
import com.framework.config.ProjectConfig;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Класс для базовой настройки автотестов.
 */
public class TestBase {
    private static final ProjectConfig config = ConfigReader.Instance();

    /**
     * Настройка.
     */
    @BeforeSuite
    static void setup() {
        // Selenide configuration
        Configuration.browser = config.browser();
        Configuration.browserVersion = config.browserVersion();
        Configuration.browserSize = config.browserSize();
        Configuration.baseUrl = config.baseUrl();
        Configuration.headless = config.headless();
        Configuration.timeout = config.timeout();

        // Allure Selenide listener
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        // RestAssured configuration
        //RestAssured.baseURI = config.apiBaseUrl();
        RestAssured.requestSpecification = ApiSpecs.getDefaultRequestSpec();
        RestAssured.responseSpecification = ApiSpecs.getDefaultResponseSpec();

    }

    /**
     * Выход из теста.
     */
    @AfterMethod
    void tearDown() {
        //BrowserFactory.clearCookies();
        RestAssured.reset();
    }
}
