package com.framework.listeners;

import com.framework.utils.logger.TestLogger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestExecutionListener implements ITestListener {
    private static final TestLogger log = new TestLogger(TestExecutionListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.initTest(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.finishTest(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            log.error("Test failed: {}", result.getName(), result.getThrowable());
            log.finishTest(result);
        } catch (Exception e) {
            System.err.println("Ошибка логирования в onTestFailure: " + e.getMessage());
            //log.error("Ошибка логирования в onTestFailure: ", result.getName(), result.getThrowable());
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try {
            log.error("Test failed: {}", result.getName(), result.getThrowable());
        } catch (Exception e) {
            System.err.println("Ошибка логирования в onTestSkipped: " + e.getMessage());
            //log.error("Ошибка логирования в onTestFailure: ", result.getName(), result.getThrowable());
            e.printStackTrace();
        }
    }
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.error("Test failed but within success percentage: {}", result.getName(), result.getThrowable());
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("Test execution" +
                " started: {}", context.getName());
    }
    @Override
    public void onFinish(ITestContext context) {
    }
}
