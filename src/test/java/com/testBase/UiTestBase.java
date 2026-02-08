package com.testBase;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.framework.listeners.SelenideListener;
import com.framework.ui.browserFactory.BrowserFactory;
import com.framework.utils.logger.TestLogger;
import com.framework.utils.screenshots.Screenshots;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.*;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

/**
 * Базовый класс для UI тестов.
 */
public class UiTestBase extends TestBase {
    private static final TestLogger log = new TestLogger(UiTestBase.class);

    @BeforeSuite
    public void setupSelenideListener() {
        SelenideLogger.addListener("CustomSelenideListener", new SelenideListener());
        log.info("Selenide listener добавлен");
    }

    @BeforeMethod
    @Parameters({"browser", "browserVersion"})
    public void setup(@Optional("chrome") String browser, @Optional("") String browserVersion) {
        // Jenkins/Maven -Dbrowser.name overrides suite parameter
        String actualBrowser = System.getProperty("browser.name", browser);
        String actualVersion = System.getProperty("browser.version", browserVersion != null ? browserVersion : "");
        BrowserFactory.setupBrowser(actualBrowser, actualVersion);
        open("");
    }

    @AfterMethod
    public void tearDown() {
        Screenshots.takeScreenshot("Завершение теста");
        BrowserFactory.clearCookies();
        closeWebDriver();
    }
}
