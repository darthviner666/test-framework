package com.framework.asserts;

import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;
import org.testng.asserts.Assertion;

/**
 * Класс для Assertions с логированием в Allure.
 */

public class AssertionsWithAllureLog {
    /**
     * Экземпляр Assertion для выполнения проверок.
     */
    private static final Assertion assertion = new Assertion();
    /**
     * Логгер для логирования событий.
     * Используется для записи информации о сравнении значений.
     */
    private static final TestLogger log = new TestLogger(AssertionsWithAllureLog.class);

    /**
     * Сравнить значения на равенство.
     * @param actual - фактическое.
     * @param expected - ожидаемое.
     * @param comment - комментарий.
     */
    @Step("Сравнить на равенство {comment}")
    public static void assertEquals(Object actual, Object expected, String comment) {
        String message = String.format("Ожидалось: '%s', получено: '%s'", expected.toString(), actual.toString());
        Allure.addAttachment("Детали сравнения", "text/plain", message);
        log.info("Сравнение значений: ожидаемое = {}, фактическое = {}", expected.toString(), actual.toString());
        assertion.assertEquals(actual,expected,comment);
        log.info("Сравнение завершено успешно");
    }


    /**
     * Сравнить значения на неравенство.
     * @param actual - фактическое.
     * @param expected - ожидаемое.
     * @param comment - комментарий.
     */
    @Step("Сравнить на неравенство {comment}")
    public static void assertNotEquals(Object actual, Object expected, String comment) {
        String message = String.format("Ожидалось: '%s', получено: '%s'", expected.toString(), actual.toString());
        Allure.addAttachment("Детали сравнения", "text/plain", message);
        log.info("Сравнение значений: ожидаемое = {}, фактическое = {}", expected.toString(), actual.toString());
        assertion.assertNotEquals(actual,expected,comment);
        log.info("Сравнение завершено успешно");
    }


    /**
     * Сравнить логичеки на правду.
     * @param actual - фактическое.
     * @param comment - комментарий.
     */
    @Step("Сравнить логичеки на правду {comment}")
    public static void assertTrue(Boolean actual, String comment) {
        String message = String.format("Ожидалось: "+ actual.toString());
        Allure.addAttachment("Детали сравнения", "text/plain", message);
        log.info("Проверка логического значения: фактическое = {}", actual.toString());
        assertion.assertTrue(actual, comment);
        if (actual) {
            log.info("Проверка завершена успешно");
        } else {
            log.error("Проверка завершена с ошибкой");
        }
    }
}
