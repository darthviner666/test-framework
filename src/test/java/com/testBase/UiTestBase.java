package com.testBase;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.framework.listeners.SelenideListener;
import com.framework.ui.browserFactory.BrowserFactory;
import com.framework.utils.screenshots.Screenshots;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.*;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

/**
 * Базовый класс для UI тестов.
 */
public class UiTestBase extends TestBase {

    @BeforeSuite
    public void setupSelenideListener() {
        SelenideLogger.addListener("CustomSelenideListener", new SelenideListener());
    }

    @BeforeMethod
    @Parameters({"browser", "browserVersion"})
    public void setup(@Optional("chrome") String browser, @Optional("120.0") String browserVersion) {
        BrowserFactory.setupBrowser(browser, browserVersion);
        open("");
    }

    @AfterMethod
    public void tearDown() {
        Screenshots.takeScreenshot("Завершение теста");
        BrowserFactory.clearCookies();
        closeWebDriver();
    }
}
