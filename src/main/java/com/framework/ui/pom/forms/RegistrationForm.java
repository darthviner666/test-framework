package com.framework.ui.pom.forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pojo.UserUiPojo;
import com.framework.ui.pom.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

/**
 * Форма регистрации.
 */
public class RegistrationForm extends BasePage {
    protected SelenideElement form = $(".js-registration-form");

    private final SelenideElement nameInput = form.$(".js-input-name");
    private final SelenideElement emailInput = form.$("input[type='email']");
    private final SelenideElement passwordInput = form.$("input[type='password'][name='password1']");
    private final SelenideElement confirmPasswordInput = form.$("input[type='password'][name='password2']");
    private final SelenideElement phoneInput = form.$("input[type='tel']");
    private final SelenideElement agreeCheckBox = form.$("[name='AGREE']");
    private final SelenideElement submitBtn = form.$("button[type='submit']");
    private final SelenideElement haveChildrenCheckBox = form.$(byXpath(".//i[text()='Есть дети']"));
    private final SelenideElement havePregnancyCheckBox = form.$(byXpath(".//i[text()='Беременность']"));

    /**
     * Заполнить имя.
     *
     * @param text - текст.
     */
    @Step("Заполнить имя на форме регистрации значением {text}")
    public RegistrationForm fillName(String text) {
        nameInput.sendKeys(text);
        return this;
    }

    /**
     * Заполнить email.
     *
     * @param text - текст.
     */
    @Step("Заполнить email на форме регистрации значением {text}")
    public RegistrationForm fillEmail(String text) {
        emailInput.sendKeys(text);
        return this;
    }


    /**
     * Заполнить пароль.
     *
     * @param text - текст.
     */
    @Step("Заполнить пароль на форме регистрации значением {text}")
    public RegistrationForm fillPassword(String text) {
        passwordInput.sendKeys(text);
        return this;
    }


    /**
     * Заполнить подтверждение пароля.
     *
     * @param text - текст.
     */
    @Step("Заполнить подтверждение пароля на форме регистрации значением {text}")
    public RegistrationForm fillConfirmPassword(String text) {
        confirmPasswordInput.sendKeys(text);
        return this;
    }

    /**
     * Заполнить телефон.
     *
     * @param text - текст.
     */
    @Step("Заполнить телефон на форме регистрации значением {text}")
    public RegistrationForm fillPhone(String text) {
        phoneInput.sendKeys(text);
        return this;
    }

    /**
     * Нажать чекбокс 'Есть дети'.
     */
    @Step("Нажать чекбокс 'Есть дети'")
    public RegistrationForm haveChildrenCheck() {
        haveChildrenCheckBox.click();
        return this;
    }

    /**
     * Нажать чекбокс 'Беременность'.
     */
    @Step("Нажать чекбокс 'Беременность'")
    public RegistrationForm havePregnancyCheck() {
        havePregnancyCheckBox.click();
        return this;
    }

    /**
     * Нажать чекбокс 'Согласие'.
     */
    @Step("Нажать чекбокс 'Согласие'")
    public RegistrationForm agreeCheck() {
        agreeCheckBox.click();
        return this;
    }

    /**
     * Нажать чекбокс 'Согласие'.
     */
    @Step("Нажать кнопку 'Зарегистрироваться'")
    public RegistrationForm clickSubmit() {
        submitBtn.click();
        return this;
    }

    /**
     * Заполнить форму 'Регистрация'.
     */
    @Step("Заполнить форму 'Регистрация'.")
    public RegistrationForm fillRegistrationForm(UserUiPojo user) {
        fillName(user.name);
        fillEmail(user.email);
        fillPassword(user.password);
        fillConfirmPassword(user.password);
        fillPhone(user.phone);
        /* лень
        if (user.haveChildren)
            havePregnancyCheck();
        if (user.haveChildren)
            haveChildrenCheck(); */
        clickSubmit();
        return this;
    }

    @Override
    @Step("Загрузка формы 'Регистрация'")
    public void isLoaded() {
        form.shouldBe(Condition.visible);
        nameInput.shouldBe(Condition.visible);
        emailInput.shouldBe(Condition.visible);
        passwordInput.shouldBe(Condition.visible);
        confirmPasswordInput.shouldBe(Condition.visible);
        haveChildrenCheckBox.shouldBe(Condition.visible);
        havePregnancyCheckBox.shouldBe(Condition.visible);
        agreeCheckBox.shouldBe(Condition.visible);
    }
}
