package com.framework.ui.pom.forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pom.elements.Button;
import com.framework.ui.pom.elements.Form;
import com.framework.ui.pom.elements.anotations.UiElement;
import com.framework.ui.pom.pages.BasePage;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.localStorage;

/**
 * Форма 'Принять Cookie'.
 */
public class CookieForm extends BasePage {
    /**
     * Логгер для логирования событий.
     */
    private final TestLogger testLogger = new TestLogger(CookieForm.class);

    @UiElement(css = "div.cookie__bg", name = "Форма запроса куки")
    private Form form;
    @UiElement(css = "button[class='cookie__btn btn js-accept-cookies']", name = "Подтвердить")
    public Button confirmButton;

    @Override
    @Step("Загрузка окна 'Принять Cookies'")
    public void isLoaded() {
        form.waitForVisibility();
        confirmButton.waitForVisibility();
        testLogger.info(form.getName() + "загружена");
    }
}
