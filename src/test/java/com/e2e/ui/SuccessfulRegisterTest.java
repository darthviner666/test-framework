package com.e2e.ui;

import com.testBase.UiTestBase;
import com.framework.ui.pojo.UserUiPojo;
import com.framework.ui.pom.MainPage;
import com.framework.ui.pom.RegisterPage;
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
        mainPage.getCookieForm().clickConfirm();
        mainPage.getHeader().clickRegister();

        RegisterPage registerPage = new RegisterPage();
        registerPage.isLoaded();

        UserUiPojo user = UiUserGenerator.generateUser();

        registerPage.getRegistrationForm().fillRegistrationForm(user);

        SuccesfulRegisteredPopup popup = new SuccesfulRegisteredPopup();
        popup.isLoaded();
        popup.clickConfirm();

        mainPage.isLoaded();
    }
}