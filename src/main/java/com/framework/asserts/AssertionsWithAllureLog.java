package com.framework.asserts;

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
     * Сравнить значения на равенство.
     * @param actual - фактическое.
     * @param expected - ожидаемое.
     * @param comment - комментарий.
     */
    @Step("Сравнить на равенство {comment}")
    public void assertEquals(Object actual, Object expected, String comment) {
        String message = String.format("Ожидалось: '%s', получено: '%s'", expected.toString(), actual.toString());
        Allure.addAttachment("Детали сравнения", "text/plain", message);
        assertion.assertEquals(actual,expected,comment);
    }


    /**
     * Сравнить значения на неравенство.
     * @param actual - фактическое.
     * @param expected - ожидаемое.
     * @param comment - комментарий.
     */
    @Step("Сравнить на неравенство {comment}")
    public void assertNotEquals(Object actual, Object expected, String comment) {
        String message = String.format("Ожидалось: '%s', получено: '%s'", expected.toString(), actual.toString());
        Allure.addAttachment("Детали сравнения", "text/plain", message);
        assertion.assertNotEquals(actual,expected,comment);
    }


    /**
     * Сравнить логичеки на правду.
     * @param actual - фактическое.
     * @param comment - комментарий.
     */
    @Step("Сравнить логичеки на правду {comment}")
    public void assertTrue(Boolean actual, String comment) {
        String message = String.format("Ожидалось: "+ actual.toString());
        Allure.addAttachment("Детали сравнения", "text/plain", message);
        assertion.assertTrue(actual, comment);
    }
}
