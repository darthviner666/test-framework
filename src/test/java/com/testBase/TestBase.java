package com.testBase;

import com.framework.utils.logger.TestLogger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Класс для базовой настройки автотестов.
 */
public class TestBase {

    protected final static TestLogger log = new TestLogger(TestBase.class);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        log.initSuite(context.getSuite().getName());
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
        log.finishSuite(context.getSuite().getName());
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestResult result) {
        log.initTest(result);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        System.out.println();
    }

}
