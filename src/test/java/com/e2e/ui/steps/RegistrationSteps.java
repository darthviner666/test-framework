package com.e2e.ui.steps;

import com.framework.ui.pom.pages.RegisterPage;
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
        registerPage.getRegistrationForm().nameInput.setValue(name);
    }


    @When("Пользователь вводит {string} в поле Телефон")
    public void fillPhoneField(String phone) {
        registerPage.getRegistrationForm().phoneInput.setValue(phone);
    }

    @When("Пользователь вводит {string} в поле Пароль")
    public void fillPassword(String password) {
        registerPage.getRegistrationForm().passwordInput.setValue(password);
    }

    @When("Пользователь вводит {string} в поле Подтвердите Пароль")
    public void fillConfirmPassword(String password) {
        registerPage.getRegistrationForm().confirmPasswordInput.setValue(password);
    }

    @When("Пользователь нажимает кнопку 'Принимаю' в форме подтверждения куки")
    public void confirmCookies() {
        registerPage.getCookieForm().isLoaded();
        registerPage.getCookieForm().confirmButton.click();
    }

    @When("Пользователь нажимает кнопку 'Зарегистрироваться'")
    public void clickRegisterButton() {
        registerPage.getRegistrationForm().submitBtn.click();
    }

    @Then("Отображается сообщение об успешной регистрации")
    public void registerSuccessfully() {
        SuccesfulRegisteredPopup popup = new SuccesfulRegisteredPopup();
        popup.isLoaded();
        popup.confirmBtn.click();
    }

    @And("Пользователь вводит {string} в поле Email")
    public void fillEmail(String email) {
        registerPage.getRegistrationForm().emailInput.setValue(email);
    }
}
