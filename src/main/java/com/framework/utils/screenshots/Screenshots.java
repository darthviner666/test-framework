package com.framework.utils.screenshots;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;

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
                new ByteArrayInputStream(Selenide.screenshot(OutputType.BYTES)));
    }
}
