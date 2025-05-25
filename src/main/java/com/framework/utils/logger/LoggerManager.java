package com.framework.utils.logger;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public void step(String message) {
        logger.info(STEP, "▷ {}", message);
    }
}
