package com.framework.listeners;

import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import com.framework.utils.logger.TestLogger;

/**
 * Класс для логирования событий Selenide.
 */
public class SelenideListener implements LogEventListener {
    private static final TestLogger log = new TestLogger(SelenideListener.class);

    /**
     * Конструктор класса SelenideListener.
     * Инициализирует логирование событий Selenide.
     */
    public SelenideListener() {
        log.info("Selenide listener initialized");
    }
    /**
     * Метод, вызываемый перед выполнением события Selenide.
     * Логирует начало события.
     *
     * @param currentLog Текущее событие логирования.
     */
    @Override
    public void beforeEvent(LogEvent currentLog) {
        log.info("Selenide START: {} | {}", currentLog.getElement(), currentLog.getSubject());
    }
/**
     * Метод, вызываемый после выполнения события Selenide.
     * Логирует результат события в зависимости от его статуса.
     *
     * @param currentLog Текущее событие логирования.
     */
    @Override
    public void afterEvent(LogEvent currentLog) {
        switch (currentLog.getStatus()) {
            case PASS:
                log.info("Selenide PASS: {} | {} | {}ms",
                        currentLog.getElement(),
                        currentLog.getSubject(),
                        currentLog.getDuration());
                break;
            case FAIL:
                log.error("Selenide FAIL: {} | {} | {}ms",
                        currentLog.getElement(),
                        currentLog.getSubject(),
                        currentLog.getDuration());
                break;
        }
    }

}
