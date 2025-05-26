package com.framework.listeners;

import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import com.framework.utils.logger.TestLogger;
import org.slf4j.LoggerFactory;

public class SelenideListener implements LogEventListener {
    private static final TestLogger log = new TestLogger(SelenideListener.class);

    @Override
    public void beforeEvent(LogEvent currentLog) {
        log.debug("Selenide START: {} | {}", currentLog.getElement(), currentLog.getSubject());
    }

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
