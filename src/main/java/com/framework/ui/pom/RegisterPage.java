package com.framework.ui.pom;

import com.codeborne.selenide.Condition;
import com.framework.ui.pom.forms.CookieForm;
import com.framework.ui.pom.forms.RegistrationForm;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;

/**
 * Страница регистрации.
 */
public class RegisterPage extends BasePage {
    /**
     * Логгер для логирования событий.
     */
    private static final TestLogger log = new TestLogger(RegisterPage.class);

    /**
     * Форма регистрации.
     */
    private final RegistrationForm registrationForm = new RegistrationForm();

    /**
     * Геттер для формы регистрации.
     */
    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    /**
     * Форма для работы с куками.
     */
    private final CookieForm cookieForm = new CookieForm();

    /**
     * Геттер для формы работы с куками.
     * Используется для взаимодействия с формой, которая появляется при первом посещении сайта.
     */
    public CookieForm getCookieForm() {
        return cookieForm;
    }
    /**
     * Проверка загрузки.
     */
    @Override
    @Step("Загрузка страницы регистрации")
    public void isLoaded() {
        log.logStep("Проверка загрузки страницы регистрации");
        form.shouldBe(Condition.visible);
        registrationForm.isLoaded();
        log.info("Страница регистрации загружена успешно");
    }
}