package com.framework.ui.pom.pages;

import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pom.elements.Form;
import com.framework.ui.pom.elements.anotations.UiElement;
import com.framework.ui.pom.elements.anotations.UiElementProcessor;

import java.lang.reflect.AnnotatedArrayType;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;

/**
 * Базовая страница POM.
 */
public abstract class BasePage {

    @UiElement(css = ".page__wrapper", name = "Форма")
    protected Form form;

    public BasePage() {
        UiElementProcessor.initPageObject(this);
    }

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
