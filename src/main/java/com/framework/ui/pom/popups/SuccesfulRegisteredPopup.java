package com.framework.ui.pom.popups;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pom.elements.Button;
import com.framework.ui.pom.elements.Form;
import com.framework.ui.pom.elements.anotations.UiElement;
import com.framework.ui.pom.pages.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

/**
 * Поп-ап об успешной регистрации.
 */
public class SuccesfulRegisteredPopup extends BasePage {
    @UiElement(css = ".popup__content", name = "Форма регистрации")
    private Form form;
    @UiElement(css = "a[class='popup__close btn btn_small']", name = "Подтвердить")
    public Button confirmBtn;

    @Override
    @Step("Загрузка поп-апа о усешной регистрации")
    public void isLoaded() {
        form.waitForVisibility();
        confirmBtn.waitForVisibility();
    }
}
