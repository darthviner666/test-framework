package com.ui;

import com.TestBase;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.framework.ui.browserFactory.BrowserFactory;
import com.framework.utils.screenshots.Screenshots;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

/**
 * Базовый класс для UI тестов.
 */
public class UiTestBase extends TestBase {
    @BeforeMethod
    @Parameters({"browser", "browserVersion"})
    public void setup(@Optional("chrome") String browser, @Optional("120.0") String browserVersion) {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
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
