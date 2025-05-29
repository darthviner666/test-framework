package com.framework.listeners;

import com.framework.utils.logger.TestLogger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class RetryListener implements IRetryAnalyzer, ITestListener {
    private static final TestLogger log = new TestLogger(RetryListener.class);
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            return true;
        }
        return false;
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (retry(result)) {
            log.info("Попытка перезапуска теста: " + result.getName());
        }
    }
}
