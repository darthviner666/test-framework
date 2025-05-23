package com.framework.ui.pom;

import com.codeborne.selenide.Condition;
import com.framework.ui.pom.forms.RegistrationForm;
import io.qameta.allure.Step;

/**
 * Страница регистрации.
 */
public class RegisterPage extends BasePage {
    /**
     * Форма регистрации.
     */
    public RegistrationForm registrationForm = new RegistrationForm();
    /**
     *  Проверка загрузки.
     */
    @Override
    @Step("Загрузка страницы регистрации")
    public void isLoaded() {
        form.shouldBe(Condition.visible);
    }
}
