package com;

import com.codeborne.selenide.Selenide;
import com.framework.api.restAssured.ApiSpecs;
import com.framework.config.ConfigReader;
import com.framework.config.ProjectConfig;
import com.framework.utils.logger.LoggerManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Класс для базовой настройки автотестов.
 */
public class TestBase {
    private static final ProjectConfig config = ConfigReader.Instance();
    protected final LoggerManager log = LoggerManager.getInstance();
    /**
     * Настройка.
     */
    @BeforeSuite
    static void setup() {

        // RestAssured configuration
        //RestAssured.baseURI = config.apiBaseUrl();
        RestAssured.requestSpecification = ApiSpecs.getDefaultRequestSpec();
        RestAssured.responseSpecification = ApiSpecs.getDefaultResponseSpec();

    }

    @BeforeMethod
    public void beforeMethod() {
        log.testStart(getTestName());
    }
    @AfterMethod
    public void afterMethod(ITestResult result) {
        String status = result.isSuccess() ? "PASSED" : "FAILED";
        log.testEnd(getTestName(), status);

        // Прикрепляем логи к Allure
        Allure.addAttachment("Test Logs",
                "text/plain",
                getTestLogs());
    }

    private String getTestLogs() {
        // Здесь можно реализовать сбор логов из файла или буфера
        return "Логи теста...";
    }

    @Step("{0}")
    protected void step(String message) {
        log.step(message);
    }

    private String getTestName() {
        return this.getClass().getSimpleName() + "." +
                Thread.currentThread().getStackTrace()[3].getMethodName();
    }

    private String getTestStatus() {
        return Selenide.webdriver().driver().hasWebDriverStarted() ? "PASSED" : "FAILED";
    }

    /**
     * Выход из теста.
     */
    @AfterMethod
    void tearDown() {
        RestAssured.reset();
    }
}
