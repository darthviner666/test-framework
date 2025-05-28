package com.framework.utils.logger;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Класс для логирования тестов с использованием Log4j и Allure.
 * Позволяет записывать логи в файл, а также добавлять шаги и результаты тестов в Allure.
 */

public class TestLogger {
    /**
     * Формат даты и времени для логов.
     * Используется для форматирования временных меток в логах.
     */
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    /**
     * Логгер для логирования событий тестов.
     * Используется для записи информации о начале и завершении тестов, а также для логирования ошибок.
     */
    private final Logger log;
    /**
     * Время начала теста.
     * Используется для измерения продолжительности выполнения теста.
     */
    private long testStartTime;
    /**
     * Время начала тестового набора.
     * Используется для измерения продолжительности выполнения всего набора тестов.
     */
    private long suiteStartTime;
    /**
     * Уникальный идентификатор теста.
     * Генерируется для каждого теста и используется для создания уникальных лог-файлов.
     */
    private String uniqueid;
    /**
     * Имя теста.
     * Используется для идентификации текущего теста и создания соответствующих лог-файлов.
     */
    private String testName;
    /**
     * Путь к файлу логов теста.
     * Используется для сохранения логов каждого теста в отдельный файл.
     */
    private String logTestFilePath;
    /**
     * Путь к файлу логов тестового набора.
     * Используется для сохранения логов всего тестового набора в отдельный файл.
     */
    private String logSuiteFilePath;

    /**
     * Конструктор класса TestLogger.
     * Инициализирует логгер для указанного класса теста.
     *
     * @param testClass Класс теста, для которого будет использоваться логгер.
     */
    public TestLogger(Class<?> testClass) {
        this.log = LogManager.getLogger(testClass);
    }

    public void initSuite(String suiteName) {
        this.suiteStartTime = System.currentTimeMillis();
        this.logSuiteFilePath = String.format("target/logs/suite_%s_%s.log",
                suiteName,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
        ThreadContext.put("logFile", logSuiteFilePath);
        ensureLogDirectoryExists();
        log.info("Начало выполнения тестового набора: {}", suiteName);
    }

    /**
     * Инициализирует тест, устанавливая имя теста, время начала и создавая уникальный идентификатор.
     * Также настраивает контекст логирования и создает директорию для логов.
     *
     * @param result Результат теста, содержащий информацию о методе.
     */
    public void initTest(ITestResult result) {
        this.testName = result.getMethod().getMethodName();
        this.testStartTime = System.currentTimeMillis();
        this.uniqueid = UUID.randomUUID().toString().substring(0, 6);
        this.logTestFilePath = String.format("target/logs/%s_%s_%s",
                testName,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")),
                uniqueid);

        ThreadContext.put("testName", testName);
        ThreadContext.put("logFile", logTestFilePath);

        ensureLogDirectoryExists();
        logTestStart();
    }

    /**
     * Завершает тест, записывая статус и продолжительность выполнения.
     * Также добавляет логи в Allure и очищает контекст логирования.
     *
     * @param result Результат теста, содержащий информацию о методе.
     */
    public void finishTest(ITestResult result) {
        long duration = System.currentTimeMillis() - testStartTime;
        String status = result.isSuccess() ? "ПРОЙДЕН" : "ПРОВАЛЕН";

        logTestEnd(status, duration);
        attachLogsToAllure(logTestFilePath);
        ThreadContext.clearAll();
    }

    /**
     * Логирует шаг в тесте, добавляя временную метку и сообщение.
     * Сообщение записывается в файл и выводится в консоль.
     *
     * @param message Сообщение шага, которое будет записано в лог.
     */
    public void logStep(String message) {
        String timestamp = dtf.format(LocalDateTime.now());
        String logMessage = String.format("[ШАГ] %s: %s", timestamp, message);

        log.info(logMessage); // Пишем в файл и консоль
        //Allure.step(logMessage); // Добавляем шаг в Allure
    }

    /**
     * Проверяет наличие директории для логов и создает ее, если она не существует.
     * Используется для обеспечения наличия места для сохранения логов тестов.
     */
    private void ensureLogDirectoryExists() {
        try {
            Files.createDirectories(Paths.get("target/logs"));
        } catch (IOException e) {
            log.error("Не удалось создать директорию для логов", e);
        }
    }

    /**
     * Логирует начало теста, записывая информацию о тесте и пути к логам.
     * Также добавляет информацию в Allure как вложение.
     */
    private void logTestStart() {
        String startMessage = formatTestMessage(
                "═══════════════════════════════════════════",
                "🚀 НАЧАЛО ТЕСТА: " + testName,
                "📁 Логи будут сохранены в: " + logTestFilePath + ".log",
                "═══════════════════════════════════════════"
        );

        log.info(startMessage);
        Allure.addAttachment("Начало теста", "text/plain", startMessage);
    }

    /**
     * Логирует окончание теста, записывая статус и продолжительность выполнения.
     * Также добавляет информацию в Allure как вложение.
     *
     * @param status   Статус теста (ПРОЙДЕН или ПРОВАЛЕН).
     * @param duration Продолжительность выполнения теста в миллисекундах.
     */
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

    /**
     * Прикрепляет логи теста к отчету Allure.
     * Читает содержимое файла логов и добавляет его как вложение в Allure.
     *
     * @param logPath Путь к файлу логов теста.
     */
    private void attachLogsToAllure(String logPath) {
        try {
            String logs = Files.readString(Paths.get(logPath + ".log"));
            Allure.addAttachment("Полные логи теста", "text/plain", logs);
        } catch (IOException e) {
            log.error("Не удалось прочитать логи теста", e);
        }
    }

    /**
     * Форматирует сообщение для теста, объединяя несколько строк в одно сообщение.
     * Используется для создания структурированных логов с разделителями.
     *
     * @param lines Строки, которые будут объединены в одно сообщение.
     * @return Форматированное сообщение.
     */
    private String formatTestMessage(String... lines) {
        return String.join("\n", lines);
    }

    public void logScreenshot(String description, String screenshotPath) {
        log.info("Скриншот: {} - {}", description, screenshotPath);
        Allure.addAttachment(description, "image/png",
                Path.of(screenshotPath).toAbsolutePath().toString());
    }

    /**
     * Логирует запрос и ответ API, добавляя их в Allure как вложение.
     * Используется для отслеживания взаимодействия с API в тестах.
     *
     * @param request  Запрос, отправленный к API.
     * @param response Ответ, полученный от API.
     */
    public void logRequestResponse(String request, String response) {
        String message = String.format("Request:\n%s\nResponse:\n%s", request, response);
        log.debug(message);
        Allure.addAttachment("API Request/Response", "text/plain", message);
    }

    /**
     * Логирует информационное сообщение.
     * Используется для записи общих информационных сообщений в лог.
     *
     * @param message Сообщение, которое будет записано в лог.
     */
    public void info(String message) {
        log.info(message);
    }

    /**
     * Логирует отладочное сообщение.
     * Используется для записи отладочной информации в лог.
     *
     * @param message   Сообщение, которое будет записано в лог.
     * @param testName  Имя теста.
     * @param throwable Исключение, связанное с сообщением (если есть).
     */
    public void error(String message, String testName, Throwable throwable) {
        log.error(message, testName, throwable);
    }

    /**
     * Логирует отладочное сообщение с элементом и субъектом.
     * Используется для записи отладочной информации с дополнительными параметрами.
     *
     * @param s       Сообщение, которое будет записано в лог.
     * @param element Элемент, связанный с сообщением.
     * @param subject Субъект, связанный с сообщением.
     */
    public void debug(String s, String element, String subject) {
        log.debug(s, element, subject);
    }

    /**
     * Логирует информационное сообщение с элементом, субъектом и продолжительностью.
     * Используется для записи информации о выполнении действий в тестах.
     *
     * @param s        Сообщение, которое будет записано в лог.
     * @param element  Элемент, связанный с сообщением.
     * @param subject  Субъект, связанный с сообщением.
     * @param duration Продолжительность выполнения действия в миллисекундах.
     */
    public void info(String s, String element, String subject, long duration) {
        log.info(s, element, subject, duration);
    }

    /**
     * Логирует ошибку с элементом, субъектом и продолжительностью.
     * Используется для записи ошибок, связанных с выполнением действий в тестах.
     *
     * @param s        Сообщение об ошибке, которое будет записано в лог.
     * @param element  Элемент, связанный с сообщением.
     * @param subject  Субъект, связанный с сообщением.
     * @param duration Продолжительность выполнения действия в миллисекундах.
     */
    public void error(String s, String element, String subject, long duration) {
        log.error(s, element, subject, duration);
    }

    /**
     * Логирует информационное сообщение с методом и URI.
     * Используется для записи информации о выполнении HTTP-запросов в тестах.
     *
     * @param s      Сообщение, которое будет записано в лог.
     * @param method Метод HTTP-запроса (например, GET, POST).
     * @param uri    URI запроса.
     */
    public void info(String s, String method, String uri) {
        log.info(s, method, uri);
    }

    /**
     * Логирует отладочное сообщение с телом запроса или ответа.
     * Используется для записи отладочной информации, связанной с содержимым запросов или ответов.
     *
     * @param s    Сообщение, которое будет записано в лог.
     * @param body Тело запроса или ответа, которое будет записано в лог.
     * @param <T>  Тип тела запроса или ответа.
     */
    public <T> void debug(String s, T body) {
        log.debug(s, body);
    }

    /**
     * Логирует информационное сообщение с телом запроса или ответа.
     * Используется для записи информации о содержимом запросов или ответов.
     *
     * @param s    Сообщение, которое будет записано в лог.
     * @param body Тело запроса или ответа, которое будет записано в лог.
     * @param <T>  Тип тела запроса или ответа.
     */
    public <T> void info(String s, T body) {
        log.debug(s, body);
    }

    /**
     * Логирует информационное сообщение с текстом и именем.
     * Используется для записи общих информационных сообщений с дополнительной информацией.
     *
     * @param s    Сообщение, которое будет записано в лог.
     * @param name Имя, связанное с сообщением (например, имя теста).
     */
    public void info(String s, String name) {
        log.info(s, name);
    }

    /**
     * Завершает тестовый набор, записывая информацию о его завершении и статистику выполнения.
     * Также добавляет логи в Allure и очищает контекст логирования.
     *
     * @param context Контекст тестового набора, содержащий информацию о выполненных тестах.
     */
    public void finishSuite(ITestContext context) {
        ThreadContext.put("logFile", logSuiteFilePath);
        log.info("Завершение тестового набора: {}", context.getSuite().getName());
        String endMessage = formatTestMessage(
                "═══════════════════════════════════════════",
                "✅ ТЕСТОВЫЙ НАБОР ЗАВЕРШЕН: " + context.getSuite().getName(),
                "⏱ Время выполнения: " + (System.currentTimeMillis() - suiteStartTime) + " мс",
                "📁 Логи сохранены в: " + logSuiteFilePath + ".log",
                "📅 Дата и время завершения: " + LocalDateTime.now().format(dtf),
                "✅️ Успешных тестов: " + context.getPassedTests().size(),
                "❗️ Проваленных тестов: " + context.getFailedTests().size(),
                "❗️ Пропущенных тестов: " + context.getSkippedTests().size(),
                "❗️ Неудачных тестов: " + context.getFailedButWithinSuccessPercentageTests().size(),
                "═══════════════════════════════════════════"
        );
        log.info(endMessage);
        attachLogsToAllure(logSuiteFilePath);
        Allure.addAttachment("Завершение тестового набора", "text/plain", endMessage);
        ThreadContext.clearAll();
    }

    /**
     * Логирует ошибку с сообщением.
     * Используется для записи ошибок, связанных с выполнением тестов.
     *
     * @param message Сообщение об ошибке, которое будет записано в лог.
     */
    public void error(String message) {
        log.error(message);
    }

    /**
     * Логирует ошибку с сообщением и дополнительной информацией.
     * Используется для записи ошибок, связанных с выполнением тестов.
     *
     * @param s       Сообщение об ошибке, которое будет записано в лог.
     * @param message Дополнительная информация об ошибке.
     */
    public void error(String s, String message) {
        log.error(s, message);
    }

    /**
     * Логирует предупреждение о попытке сериализации объекта, который равен null.
     * Используется для предупреждения о потенциальных проблемах с сериализацией.
     *
     * @param attemptedToSerializeNullObject Сообщение, указывающее на попытку сериализации null-объекта.
     */
    public void warn(String attemptedToSerializeNullObject) {
        log.warn(attemptedToSerializeNullObject);
    }

    /**
     * Логирует ошибку с сообщением и исключением.
     * Используется для записи ошибок, связанных с выполнением тестов.
     *
     * @param s         Сообщение об ошибке, которое будет записано в лог.
     * @param throwable Исключение, связанное с ошибкой.
     */
    public void error(String s, Throwable throwable) {
        if (throwable != null) {
            log.error(s, throwable);
        } else {
            log.error(s);
        }
    }

    /**
     * Логирует предупреждение с сообщением и исключением.
     * Используется для предупреждений о потенциальных проблемах.
     *
     * @param s Сообщение об ошибке, которое будет записано в лог.
     */
    public void warn(String s, Throwable throwable) {
        if (throwable != null) {
            log.warn(s, throwable);
        } else {
            log.warn(s);
        }
    }
}
