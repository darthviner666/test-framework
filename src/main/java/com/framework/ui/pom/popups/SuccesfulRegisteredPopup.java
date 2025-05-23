package com.framework.ui.pom.popups;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pom.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

/**
 * Поп-ап об успешной регистрации.
 */
public class SuccesfulRegisteredPopup extends BasePage {
    private SelenideElement form = $(".popup__content");
    private SelenideElement confirmBtn = $("a[class='popup__close btn btn_small']");

    @Step("Нажать кнопку 'На главную'")
    public SuccesfulRegisteredPopup clickConfirm() {
        confirmBtn.click();
        return this;
    }

    @Override
    @Step("Загрузка поп-апа о усешной регистрации")
    public void isLoaded() {
        form.shouldBe(Condition.visible);
        confirmBtn.shouldBe(Condition.visible);
    }
}
