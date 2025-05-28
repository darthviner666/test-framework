package com.framework.ui.pom;

import com.codeborne.selenide.Condition;
import com.framework.ui.pom.forms.CookieForm;
import com.framework.ui.pom.forms.HeaderForm;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;

/**
 * Главная страница.
 */
public class MainPage extends BasePage {
    /**
     * Логгер для логирования событий.
     */
    private static final TestLogger log = new TestLogger(MainPage.class);
    /**
     * Хэдэр.
     */
    private final HeaderForm header = new HeaderForm();

    /**
     * Форма для работы с куками.
     */
    private final CookieForm cookieForm = new CookieForm();

    /**
     * Элемент формы главной страницы.
     */
    public HeaderForm getHeader() {
        return header;
    }

    /**
     * Элемент формы для работы с куками.
     * Используется для взаимодействия с формой, которая появляется при первом посещении сайта.
     */
    public CookieForm getCookieForm() {
        return cookieForm;
    }

    /**
     * Проверка загрузки.
     */
    @Override
    @Step("Загрузка главной страницы")
    public void isLoaded() {
        log.logStep("Проверка загрузки главной страницы");
        form.shouldBe(Condition.visible);
        header.isLoaded();
        log.info("Главная страница загружена успешно");
    }
}