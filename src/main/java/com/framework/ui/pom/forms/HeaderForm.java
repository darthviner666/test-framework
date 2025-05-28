package com.framework.ui.pom.forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pom.BasePage;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

/**
 * Хэдер.
 */
public class HeaderForm extends BasePage {
    /**
     * Логгер для логирования событий.
     */
    private final static TestLogger log = new TestLogger(HeaderForm.class);
    /**
     * Кнопка регистрации.
     */
    private SelenideElement registerButton = form.$(byXpath(".//a[text()='Регистрация']"));

    @Step("Кликнуть по кнопке регистрация")
    public HeaderForm clickRegister() {
        log.logStep("Кликнуть по кнопке регистрация");
        registerButton.click();
        return this;
    }

    @Override
    @Step("Загрузка хэдэра")
    public void isLoaded() {
        log.logStep("Загрузка хэдэра");
        form.shouldBe(Condition.visible);
        registerButton.shouldBe(Condition.visible);
        log.info("Хэдер загружен");
    }
}
