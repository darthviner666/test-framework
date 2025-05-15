package com.ui;

import com.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginTest extends TestBase {
    @Test
    @DisplayName("Successful login")
    void successfulLoginTest() {
        open("/login");
        $("#username").setValue("standard_user");
        $("#password").setValue("secret_sauce");
        $("#login-button").click();

        $(".title").shouldHave(text("Products"));
    }
}
