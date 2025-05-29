package com.framework.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import com.framework.utils.logger.TestLogger;

import java.util.Arrays;

public class TestListener implements ITestListener {
    private static final TestLogger log = new TestLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Параметры: " + Arrays.toString(result.getParameters()));
    }
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