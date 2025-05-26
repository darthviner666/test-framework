package com.testBase;

import com.framework.utils.logger.TestLogger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

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
