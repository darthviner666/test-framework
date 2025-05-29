package com.framework.ui.pom.forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pojo.UserUiPojo;
import com.framework.ui.pom.BasePage;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byXpath;

/**
 * Форма регистрации.
 */
public class RegistrationForm extends BasePage {
    /**
     * Логгер для логирования событий.
     */
    private static final TestLogger log = new TestLogger(RegistrationForm.class);

    private final SelenideElement nameInput = form.$(".js-input-name");
    private final SelenideElement emailInput = form.$("input[type='email']");
    private final SelenideElement passwordInput = form.$("input[type='password'][name='password1']");
    private final SelenideElement confirmPasswordInput = form.$("input[type='password'][name='password2']");
    private final SelenideElement phoneInput = form.$("input[type='tel']");
    private final SelenideElement agreeCheckBox = form.$("[name='AGREE']");
    private final SelenideElement submitBtn = form.$("button[type='submit']");
    private final SelenideElement haveChildrenCheckBox = form.$(byXpath(".//i[text()='Есть дети']"));
    private final SelenideElement havePregnancyCheckBox = form.$(byXpath(".//i[text()='Беременность']"));

    @Step("Заполнить имя на форме регистрации значением {text}")
    public RegistrationForm fillName(String text) {
        log.logStep("Ввод имени: " + text);
        nameInput.sendKeys(text);
        return this;
    }

    @Step("Заполнить email на форме регистрации значением {text}")
    public RegistrationForm fillEmail(String text) {
        log.logStep("Ввод email: " + text);
        emailInput.sendKeys(text);
        return this;
    }

    @Step("Заполнить пароль на форме регистрации значением {text}")
    public RegistrationForm fillPassword(String text) {
        log.logStep("Ввод пароля");
        passwordInput.sendKeys(text);
        return this;
    }

    @Step("Заполнить подтверждение пароля на форме регистрации значением {text}")
    public RegistrationForm fillConfirmPassword(String text) {
        log.logStep("Ввод подтверждения пароля");
        confirmPasswordInput.sendKeys(text);
        return this;
    }

    @Step("Заполнить телефон на форме регистрации значением {text}")
    public RegistrationForm fillPhone(String text) {
        log.logStep("Ввод телефона: " + text);
        phoneInput.sendKeys(text);
        return this;
    }

    @Step("Нажать чекбокс 'Есть дети'")
    public RegistrationForm haveChildrenCheck() {
        log.logStep("Клик по чекбоксу 'Есть дети'");
        haveChildrenCheckBox.click();
        return this;
    }

    @Step("Нажать чекбокс 'Беременность'")
    public RegistrationForm havePregnancyCheck() {
        log.logStep("Клик по чекбоксу 'Беременность'");
        havePregnancyCheckBox.click();
        return this;
    }

    @Step("Нажать чекбокс 'Согласие'")
    public RegistrationForm agreeCheck() {
        log.logStep("Клик по чекбоксу 'Согласие'");
        agreeCheckBox.click();
        return this;
    }

    @Step("Нажать кнопку 'Зарегистрироваться'")
    public RegistrationForm clickSubmit() {
        log.logStep("Клик по кнопке 'Зарегистрироваться'");
        submitBtn.click();
        return this;
    }

    @Step("Заполнить форму 'Регистрация'")
    public RegistrationForm fillRegistrationForm(UserUiPojo user) {
        log.logStep("Заполнение формы регистрации данными пользователя");
        fillName(user.name);
        fillEmail(user.email);
        fillPassword(user.password);
        fillConfirmPassword(user.password);
        fillPhone(user.phone);
        // if (user.haveChildren) haveChildrenCheck();
        // if (user.havePregnancy) havePregnancyCheck();
        clickSubmit();
        return this;
    }

    @Override
    @Step("Загрузка формы 'Регистрация'")
    public void isLoaded() {
        log.logStep("Проверка загрузки формы регистрации");
        form.shouldBe(Condition.visible);
        nameInput.shouldBe(Condition.visible);
        emailInput.shouldBe(Condition.visible);
        passwordInput.shouldBe(Condition.visible);
        confirmPasswordInput.shouldBe(Condition.visible);
        haveChildrenCheckBox.shouldBe(Condition.visible);
        havePregnancyCheckBox.shouldBe(Condition.visible);
        //agreeCheckBox.shouldBe(Condition.visible);
        log.info("Форма регистрации загружена");
    }
}