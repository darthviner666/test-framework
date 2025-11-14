package com.e2e.ui;

import com.testBase.UiTestBase;
import com.framework.ui.pojo.UserUiPojo;
import com.framework.ui.pom.pages.MainPage;
import com.framework.ui.pom.pages.RegisterPage;
import com.framework.ui.pom.popups.SuccesfulRegisteredPopup;
import com.framework.utils.dataGenerators.uiUser.UiUserGenerator;
import io.qameta.allure.*;
import org.testng.annotations.Test;

/**
 * Класс для тестирования успешной регистрации пользователя через UI.
 * Тест проверяет, что пользователь может успешно зарегистрироваться на сайте.
 */
@Epic("UI Тесты")
@Feature("Регистрация")
public class SuccessfulRegisterTest extends UiTestBase {

    /**
     * Тест для проверки успешной регистрации пользователя.
     */
    @Test(description = "Успешная регистрация",
            groups = "smoke",
            priority = 2)
    @Story("Позитивный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void successfulRegisterTest() {
        MainPage mainPage = new MainPage();
        mainPage.isLoaded();
        mainPage.getCookieForm().isLoaded();
        mainPage.getCookieForm().confirmButton.click();
        mainPage.getHeader().registerButton.click();

        RegisterPage registerPage = new RegisterPage();
        registerPage.isLoaded();

        UserUiPojo user = UiUserGenerator.generateUser();

        registerPage.getRegistrationForm().nameInput.setValue(user.name);
        registerPage.getRegistrationForm().emailInput.setValue(user.email);
        registerPage.getRegistrationForm().passwordInput.setValue(user.password);
        registerPage.getRegistrationForm().confirmPasswordInput.setValue(user.password);
        registerPage.getRegistrationForm().phoneInput.setValue(user.phone);
        registerPage.getRegistrationForm().submitBtn.click();

        SuccesfulRegisteredPopup popup = new SuccesfulRegisteredPopup();
        popup.isLoaded();
        popup.confirmBtn.click();

        mainPage.isLoaded();
    }
}