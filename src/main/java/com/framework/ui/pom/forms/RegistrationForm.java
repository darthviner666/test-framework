package com.framework.ui.pom.forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pojo.UserUiPojo;
import com.framework.ui.pom.elements.Button;
import com.framework.ui.pom.elements.Checkbox;
import com.framework.ui.pom.elements.anotations.UiElement;
import com.framework.ui.pom.pages.BasePage;
import com.framework.ui.pom.elements.Input;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byXpath;

/**
 * Форма регистрации.
 */
public class RegistrationForm extends BasePage {
    /**
     * Логгер для логирования событий.
     */
    private static final TestLogger log = new TestLogger(RegistrationForm.class);

    @UiElement(css = ".js-input-name", name = "Имя")
    public Input nameInput;
    @UiElement(css = "input[type='email']", name = "Email")
    public Input emailInput;
    @UiElement(css = "input[type='password'][name='password1']", name = "Пароль")
    public Input passwordInput;
    @UiElement(css = "input[type='password'][name='password2']", name = "Подтверждение пароля")
    public Input confirmPasswordInput;
    @UiElement(css = "input[type='tel']", name = "Телефон")
    public Input phoneInput;
    @UiElement(css = "[name='AGREE']", name = "Согласен")
    public Checkbox agreeCheckBox;
    @UiElement(css = "button[type='submit']", name = "Подтвердить")
    public Button submitBtn;
    @UiElement(xpath = ".//i[text()='Есть дети']", name = "Есть дети")
    public Checkbox haveChildrenCheckBox;
    @UiElement(xpath = ".//i[text()='Беременность']", name = "Беременность")
    public Checkbox havePregnancyCheckBox;

    @Override
    @Step("Загрузка формы 'Регистрация'")
    public void isLoaded() {
        log.logStep("Проверка загрузки формы регистрации");
//        form.waitForVisibility();
        nameInput.waitForVisibility();
        emailInput.waitForVisibility();
        passwordInput.waitForVisibility();
        confirmPasswordInput.waitForVisibility();
        haveChildrenCheckBox.waitForVisibility();
        havePregnancyCheckBox.waitForVisibility();
        log.info("Форма регистрации загружена");
    }
}