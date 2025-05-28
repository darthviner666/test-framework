package com.framework.utils.screenshots;

import com.codeborne.selenide.Selenide;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Objects;

/**
 * Класс для скриншотов.
 */
public class Screenshots {
    private static final TestLogger log = new TestLogger(Screenshots.class);
    /**
     * Сделать скриншот.
     * @param screenName - имя вложения Allure.
     */
    public static void takeScreenshot(String screenName) {
        // Делаем скриншот и явно прикрепляем
        Allure.addAttachment(screenName,
                new ByteArrayInputStream(Objects.requireNonNull(Selenide.screenshot(OutputType.BYTES))));
        log.info("Сделан скриншот: {}", screenName);
    }
}
