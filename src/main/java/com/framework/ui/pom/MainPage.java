package com.framework.ui.pom;

import com.codeborne.selenide.Condition;
import com.framework.ui.pom.forms.CookieForm;
import com.framework.ui.pom.forms.HeaderForm;
import io.qameta.allure.Step;

/**
 * Главная страница.
 */
public class MainPage extends BasePage{
    /**
     * Хэдэр.
     */
    public HeaderForm header = new HeaderForm();

    public CookieForm cookieForm = new CookieForm();

    /**
     *  Проверка загрузки.
     */
    @Override
    @Step("Загрузка главной страницы")
    public void isLoaded() {
        form.shouldBe(Condition.visible);
        header.isLoaded();
    }
}
