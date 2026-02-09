package com.framework.ui.pom.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;


/**
 * Базовый класс для UI элементов.
 */
public abstract class BaseUiElement {

    protected static final TestLogger LOGGER = new TestLogger(BaseUiElement.class);
    protected SelenideElement element;
    protected String name;

    public BaseUiElement(SelenideElement element, String name) {
        this.element = element;
        this.name = name;
    }

    @Step("Проверка отображения '{this.name}'")
    public boolean isDisplayed() {
        LOGGER.logStep("Проверка отображения " + this.name);
        return element.isDisplayed();
    }

    @Step("Проверка доступности {this.name}'")
    public boolean isEnabled() {
        LOGGER.logStep("Проверка доступности " + this.name);
        return element.isEnabled();
    }

    public String getName() {
        return name;
    }

    @Step("Клик по элементу '{this.name}'")
    public BaseUiElement click() {
        LOGGER.logStep("Клик по элементу " + this.name);
        this.element.click();
        return this;
    }

    @Step("Ожидаем видимость элемента '{this.name}'")
    public BaseUiElement waitForVisibility() {
        LOGGER.logStep("Ожидаем видимость элемента " + this.name);
        this.element.shouldBe(Condition.visible);
        return this;
    }
}
