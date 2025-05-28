package com.framework.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import com.framework.utils.logger.TestLogger;

public class ExceptionLoggingListener implements ITestListener {
    private static final TestLogger log = new TestLogger(ExceptionLoggingListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            log.error("Тест упал: " + result.getName(), throwable);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            log.warn("Тест пропущен: " + result.getName(), throwable);
        }
    }
}