package com.framework.utils.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * Логгер сигнлтон.
 */
public class LoggerManager {
    private static final ThreadLocal<LoggerManager> instance = ThreadLocal.withInitial(LoggerManager::new);
    private final Logger logger;

    // Маркеры для разных типов сообщений
    private static final Marker TEST_START = MarkerManager.getMarker("TEST_START");
    private static final Marker TEST_END = MarkerManager.getMarker("TEST_END");
    private static final Marker STEP = MarkerManager.getMarker("STEP");

    private LoggerManager() {
        this.logger = LogManager.getLogger("Thread-" + Thread.currentThread().getId());
    }

    public static LoggerManager getInstance() {
        return instance.get();
    }

    public void testStart(String testName) {
        logger.info(TEST_START, "🚀 Starting test: {}", testName);
    }

    public void testEnd(String testName, String status) {
        logger.info(TEST_END, "{} Test: {} {}",
                status.equals("PASSED") ? "✅" : "❌",
                testName,
                status);
    }

    public void step(String message) {
        logger.info(STEP, "▷ {}", message);
    }

    // Дополнительные методы
    public void debug(String message) {
        logger.debug(message);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
