package com.hooks;

import com.codeborne.selenide.Selenide;
import com.framework.ui.browserFactory.BrowserFactory;
import com.framework.utils.logger.TestLogger;
import io.cucumber.java.Before;

import static com.codeborne.selenide.Selenide.open;

/**
 * Хуки для регистрации пользователя.
 * Сброс cookies перед сценарием регистрации.
 */
public class RegistrationHooks {
    TestLogger  logger = new TestLogger(RegistrationHooks.class);
    /**
     * Метод, который выполняется перед сценарием регистрации.
     * Очищает cookies и localStorage для предотвращения конфликтов с предыдущими сессиями.
     */
    @Before("@registration")
    public void beforeRegistrationScenario() {
        BrowserFactory.setupBrowser("chrome", "120.0");
        open();
        // Очистка cookies
        Selenide.clearBrowserCookies();
    }
}