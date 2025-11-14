package com.framework.ui.pom.forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pom.elements.Button;
import com.framework.ui.pom.elements.anotations.UiElement;
import com.framework.ui.pom.pages.BasePage;
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

    @UiElement(xpath = ".//a[text()='Регистрация']", name = "Регистрация")
    public Button registerButton;

    @Override
    @Step("Загрузка хэдэра")
    public void isLoaded() {
        log.logStep("Загрузка хэдэра");
        registerButton.waitForVisibility();
        log.info("Хэдер загружен");
    }
}
