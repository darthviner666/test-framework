package com.testBase;

import com.framework.database.hibernate.DatabaseHibernateActions;
import com.framework.database.hibernate.DatabaseHibernateHikariConfig;
import com.framework.listeners.RetryListener;
import com.framework.listeners.TestListener;
import com.framework.utils.logger.TestLogger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Класс для базовой настройки автотестов.
 */
@Listeners({TestListener.class, RetryListener.class})
public class TestBase {

    /**
     * Логгер для логирования событий тестов.
     * Используется для записи информации о начале и завершении тестов, а также для логирования ошибок.
     */
    protected final static TestLogger log = new TestLogger(TestBase.class);

    /**
     * Метод, выполняемый перед началом выполнения тестового набора.
     * Инициализирует логирование для всего набора тестов.
     *
     * @param context Контекст тестового набора.
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        log.initSuite(context.getSuite().getName());
        try {
            //DatabaseHibernateHikariConfig.initDatabase();
            //DatabaseHibernateActions.ensureUsersExist(20);
        } catch (Exception e) {
            log.error("Error during setup", e);
            throw e;
        }
    }

    /**
     * Метод, выполняемый после завершения выполнения тестового набора.
     * Завершает логирование для всего набора тестов.
     *
     * @param context Контекст тестового набора.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
        log.finishSuite(context);
        //DatabaseHibernateHikariConfig.shutdown();
    }

    /**
     * Метод, выполняемый перед каждым тестовым методом.
     * Инициализирует логирование для каждого теста.
     *
     * @param result Результат теста, содержащий информацию о методе.
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(ITestResult result) {
        log.initTest(result);
    }

    /**
     * Метод, выполняемый после каждого тестового метода.
     * Завершает логирование для каждого теста.
     *
     * @param result Результат теста, содержащий информацию о методе.
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        log.finishTest(result);
    }

}
