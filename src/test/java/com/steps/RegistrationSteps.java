package com.steps;

import static com.codeborne.selenide.Selenide.open;

public class RegistrationSteps {
    @Given("Пользователь находится на странице регистрации")
    public void navigateToRegistrationPage() {
         open("/register");
        // Логика для навигации на страницу регистрации
    }
}
