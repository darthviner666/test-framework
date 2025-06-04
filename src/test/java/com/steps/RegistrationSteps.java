package com.steps;

import com.framework.ui.pom.RegisterPage;
import com.framework.ui.pom.popups.SuccesfulRegisteredPopup;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.codeborne.selenide.Selenide.open;

/**
 * Шаги для регистрации пользователя.
 */
public class RegistrationSteps {
    private RegisterPage registerPage = new RegisterPage();

    @Given("Пользователь находится на странице регистрации")
    public void navigateToRegistrationPage() {
        open("/register");
        registerPage.isLoaded();

    }

    @When("Пользователь вводит {string} в поле Имя")
    public void fillNameField(String name) {
        registerPage.getRegistrationForm().fillName(name);
    }


    @When("Пользователь вводит {string} в поле Телефон")
    public void fillPhoneField(String phone) {
        registerPage.getRegistrationForm().fillPhone(phone);
    }

    @When("Пользователь вводит {string} в поле Пароль")
    public void fillPassword(String password) {
        registerPage.getRegistrationForm().fillPassword(password);
    }

    @When("Пользователь вводит {string} в поле Подтвердите Пароль")
    public void fillConfirmPassword(String password) {
        registerPage.getRegistrationForm().fillConfirmPassword(password);
    }

    @When("Пользователь нажимает кнопку 'Принимаю' в форме подтверждения куки")
    public void confirmCookies() {
        registerPage.getCookieForm().isLoaded();
        registerPage.getCookieForm().clickConfirm();
    }

    @When("Пользователь нажимает кнопку 'Зарегистрироваться'")
    public void clickRegisterButton() {
        registerPage.getRegistrationForm().clickSubmit();
    }

    @Then("Отображается сообщение об успешной регистрации")
    public void registerSuccessfully() {
        SuccesfulRegisteredPopup popup = new SuccesfulRegisteredPopup();
        popup.isLoaded();
        popup.clickConfirm();
    }

    @And("Пользователь вводит {string} в поле Email")
    public void fillEmail(String email) {
        registerPage.getRegistrationForm().fillEmail(email);
    }
}
