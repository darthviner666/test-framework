package com;

import com.codeborne.selenide.Selenide;
import com.framework.api.restAssured.ApiSpecs;
import com.framework.config.ConfigReader;
import com.framework.config.ProjectConfig;
import com.framework.utils.logger.LoggerManager;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс для базовой настройки автотестов.
 */
public class TestBase {
    protected TestLogger logger;

    @BeforeMethod
    public void beforeMethod(ITestResult result) {
        this.logger = new TestLogger(this.getClass());
        logger.initTest(result);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        logger.finishTest(result);
    }

    protected void logStep(String message) {
        logger.logStep(message);
    }

}
