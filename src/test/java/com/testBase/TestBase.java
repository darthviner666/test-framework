package com.testBase;

import com.framework.utils.logger.TestLogger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Класс для базовой настройки автотестов.
 */
public class TestBase {

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestResult result) {
        System.out.println();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        System.out.println();
    }

}
