package com.asserts;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;
import org.testng.asserts.Assertion;

/**
 * Класс для Assertions с логированием в Allure.
 */
@UtilityClass
public class AssertionsWithAllureLog {
    Assertion assertion = new Assertion();

    /**
     * Сравнить значения.
     * @param actual - фактическое.
     * @param expected - ожидаемое.
     * @param comment - комментарий.
     */
    @Step("Сравнить {comment}")
    public void assertEquals(Object actual, Object expected, String comment) {
        String message = String.format("Ожидалось: '%s', получено: '%s'", expected.toString(), actual.toString());
        Allure.addAttachment("Детали сравнения", "text/plain", message);
        assertion.assertEquals(actual,expected,comment);
    }
}
