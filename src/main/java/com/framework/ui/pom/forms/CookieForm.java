package com.framework.ui.pom.forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pom.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

/**
 * Форма 'Принять Cookie'.
 */
public class CookieForm extends BasePage {

    private SelenideElement form = $("div.cookie__bg");
    private SelenideElement confirmButton = form.$("button[class='cookie__btn btn js-accept-cookies']");

    @Step("Нажать кнопку 'Принимаю'")
    public CookieForm clickConfirm() {
        confirmButton.click();
        form.shouldBe(Condition.disappear);
        return this;
    }

    @Override
    @Step("Загрузка окна 'Принять Cookies'")
    public void isLoaded() {
        form.shouldBe(Condition.visible);
        confirmButton.shouldBe(Condition.visible);
    }
}
