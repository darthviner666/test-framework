package com.framework.ui.pom.elements;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

/**
 * Поле ввода.
 */
public class Input extends BaseUiElement {

    public Input(SelenideElement element, String name) {
        super(element, "Поле ввода " + name);
    }

    /**
     * Установить значение в поле ввода.
     * @param value - значение
     */
    @Step(value = "Ввести {value} в поле {name}")
    public BaseUiElement setValue(String value) {
        this.element.setValue(value);
        return this;
    }
}
