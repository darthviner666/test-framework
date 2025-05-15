package com;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.framework.config.ConfigReader;
import com.framework.config.ProjectConfig;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    private static final ProjectConfig config = ConfigReader.Instance();

    @BeforeAll
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
        RestAssured.baseURI = config.apiBaseUrl();
    }
}
