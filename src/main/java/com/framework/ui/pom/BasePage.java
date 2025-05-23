package com.framework.ui.pom;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;

/**
 * Базовая страница POM.
 */
public abstract class BasePage {
    protected SelenideElement form = $(".page__wrapper");

    /**
     * Получить заголовок.
     * @return - заголовок.
     */
    public String getPageTitle() {
        return title();
    }

    /**
     * Абстрактный метод для проверки загрузки страницы
     */
    public abstract void isLoaded();

}
