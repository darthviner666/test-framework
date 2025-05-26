package com.framework.listeners;

import com.framework.utils.logger.TestLogger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestExecutionListener implements ITestListener {
    private TestLogger log;

    @Override
    public void onTestStart(ITestResult result) {
        this.log = new TestLogger(this.getClass());
        log.initTest(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.finishTest(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {}", result.getName(), result.getThrowable());
        log.finishTest(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.error("Test failed: {}", result.getName(), result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
    }
}
