package com.ui;

import com.framework.ui.pojo.UserUiPojo;
import com.framework.ui.pom.MainPage;
import com.framework.ui.pom.RegisterPage;
import com.framework.ui.pom.forms.CookieForm;
import com.framework.ui.pom.popups.SuccesfulRegisteredPopup;
import com.framework.utils.dataGenerators.UiUserGenerator;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("UI Тесты")
@Feature("Регистрация")
public class SuccessfulRegisterTest extends UiTestBase {
    @Test(description = "Успешная регистрация")
    @Story("Позитивный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void succesfulRegisterTest() {
        MainPage mainPage = new MainPage();
        mainPage
                .isLoaded();
        mainPage
                .cookieForm
                .isLoaded();
        mainPage
                .cookieForm
                .clickConfirm();
        mainPage
                .header
                .clickRegister();

        RegisterPage registerPage = new RegisterPage();
        registerPage
                .isLoaded();

        UserUiPojo user = UiUserGenerator
                .generateUser();

        registerPage
                .registrationForm
                .fillRegistrationForm(user);

        SuccesfulRegisteredPopup popup = new SuccesfulRegisteredPopup();
        popup
                .isLoaded();

        popup
                .clickConfirm();

        mainPage
                .isLoaded();
    }
}
