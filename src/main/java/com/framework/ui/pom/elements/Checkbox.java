package com.framework.ui.pom.elements;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

/**
 * Чекбокс.
 */
public class Checkbox extends BaseUiElement {

    public Checkbox(SelenideElement element, String name) {
        super(element, "Чекбокс " + name);
    }

    @Override
    @Step("Клик по чекбоксу {name}")
    public BaseUiElement click() {
        this.element.click();
        return this;
    }
}
