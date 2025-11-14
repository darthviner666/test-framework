package com.framework.ui.pom.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;


/**
 * Базовый класс для UI элементов.
 */
public abstract class BaseUiElement {

    protected SelenideElement element;
    protected String name;

    public BaseUiElement(SelenideElement element, String name) {
        this.element = element;
        this.name = name;
    }

    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    public boolean isEnabled() {
        return element.isEnabled();
    }

    public String getName() {
        return name;
    }

    @Step("Клик по элементу")
    public BaseUiElement click() {
        this.element.click();
        return this;
    }

    @Step("Ожидаем видимость элемента")
    public BaseUiElement waitForVisibility() {
        this.element.shouldBe(Condition.visible);
        return this;
    }
}
