package com.framework.utils.logger;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestLogger {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final Logger log;
    private long testStartTime;
    private String testName;
    private String logFilePath;

    public TestLogger(Class<?> testClass) {
        this.log = LogManager.getLogger(testClass);
    }

    public void initTest(ITestResult result) {
        this.testName = result.getMethod().getMethodName();
        this.testStartTime = System.currentTimeMillis();
        this.logFilePath = String.format("target/logs/%s_%s.log",
                testName,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));

        ThreadContext.put("testName", testName);
        ThreadContext.put("logFile", logFilePath);

        ensureLogDirectoryExists();
        logTestStart();
    }

    public void finishTest(ITestResult result) {
        long duration = System.currentTimeMillis() - testStartTime;
        String status = result.isSuccess() ? "ПРОЙДЕН" : "ПРОВАЛЕН";

        logTestEnd(status, duration);
        attachLogsToAllure();
        ThreadContext.clearAll();
    }

    public void logStep(String message) {
        String timestamp = dtf.format(LocalDateTime.now());
        String logMessage = String.format("[ШАГ] %s: %s", timestamp, message);

        log.info(logMessage); // Пишем в файл и консоль
        Allure.step(logMessage); // Добавляем шаг в Allure
    }

    private void ensureLogDirectoryExists() {
        try {
            Files.createDirectories(Paths.get("target/logs"));
        } catch (IOException e) {
            log.error("Не удалось создать директорию для логов", e);
        }
    }

    private void logTestStart() {
        String startMessage = formatTestMessage(
                "═══════════════════════════════════════════",
                "🚀 НАЧАЛО ТЕСТА: " + testName,
                "📁 Логи будут сохранены в: " + logFilePath,
                "═══════════════════════════════════════════"
        );

        log.info(startMessage);
        Allure.addAttachment("Начало теста", "text/plain", startMessage);
    }

    private void logTestEnd(String status, long duration) {
        String statusIcon = status.equals("ПРОЙДЕН") ? "✅" : "❌";
        String endMessage = formatTestMessage(
                "═══════════════════════════════════════════",
                statusIcon + " КОНЕЦ ТЕСТА: " + testName,
                "⏱ Время выполнения: " + duration + " мс",
                "═══════════════════════════════════════════"
        );

        log.info(endMessage);
        Allure.addAttachment("Результат теста", "text/plain", endMessage);
    }

    private void attachLogsToAllure() {
        try {
            String logs = Files.readString(Path.of(logFilePath));
            Allure.addAttachment("Полные логи теста", "text/plain", logs);
        } catch (IOException e) {
            log.error("Не удалось прочитать логи теста", e);
        }
    }

    private String formatTestMessage(String... lines) {
        return String.join("\n", lines);
    }

    public void logScreenshot(String description, String screenshotPath) {
        log.info("Скриншот: {} - {}", description, screenshotPath);
        Allure.addAttachment(description, "image/png",
                Path.of(screenshotPath).toAbsolutePath().toString());
    }

    public void logRequestResponse(String request, String response) {
        String message = String.format("Request:\n%s\nResponse:\n%s", request, response);
        log.debug(message);
        Allure.addAttachment("API Request/Response", "text/plain", message);
    }
}
