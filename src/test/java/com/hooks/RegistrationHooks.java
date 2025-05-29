package com.hooks;

import com.codeborne.selenide.Selenide;
import com.framework.ui.browserFactory.BrowserFactory;
import io.cucumber.java.Before;
/**
 * Хуки для регистрации пользователя.
 * Сброс cookies перед сценарием регистрации.
 */
public class RegistrationHooks {
    /**
     * Метод, который выполняется перед сценарием регистрации.
     * Очищает cookies и localStorage для предотвращения конфликтов с предыдущими сессиями.
     */
    @Before("@registration")
    public void beforeRegistrationScenario() {
        // Очистка cookies перед сценарием регистрации
        BrowserFactory.setupBrowser("chrome", "120.0");
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }
}