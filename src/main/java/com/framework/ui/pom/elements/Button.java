package com.framework.ui.pom.elements;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

/**
 * Кнопка.
 */
public class Button extends BaseUiElement {

    public Button(SelenideElement element, String name) {
        super(element, "Кнопка " + name);
    }
}
