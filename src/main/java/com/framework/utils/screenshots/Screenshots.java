package com.framework.utils.screenshots;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.util.Objects;

/**
 * Класс для скриншотов.
 */
public class Screenshots {
    /**
     * Сделать скриншот.
     * @param screenName - имя вложения Allure.
     */
    public static void takeScreenshot(String screenName) {
        // Делаем скриншот и явно прикрепляем
        Allure.addAttachment(screenName,
                new ByteArrayInputStream(Objects.requireNonNull(Selenide.screenshot(OutputType.BYTES))));
    }
}
