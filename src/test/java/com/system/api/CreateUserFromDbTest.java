package com.system.api;

import com.framework.api.helpers.UserHelper;
import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.api.pojo.users.create.rs.CreateUserPojoRs;
import com.framework.asserts.AssertionsWithLog;
import com.framework.database.hibernate.DatabaseHibernateHikariConfig;
import com.testBase.TestBase;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CreateUserFromDbTest extends TestBase {

    private CreateUserPojoRq testUser;

    /**
     * Подготовка тестовых данных из тестовой базы данных.
     * Удаление полученного пользователя из бд, чтобы избежать
     * переиспользования тестовых данных.
     */
    @BeforeMethod
    void prepareTestUser() {
        try (Session session = DatabaseHibernateHikariConfig.getSessionFactory().openSession()) {
            testUser = session.createQuery("from CreateUserPojoRq order by RANDOM() limit 1", CreateUserPojoRq.class)
                    .getSingleResult();
            Transaction tx = session.beginTransaction();
            session.remove(testUser);
            tx.commit();
        }
    }
    /**
     * Тест для проверки создания пользователей, которых мы берём из тестовой базы.
     */
    @Test(description = "Проверка создания пользователей взятого из базы с помощью Hibernate",
            groups = "smoke",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void createUserFromDbTest() {
        UserHelper helper = new UserHelper();
        CreateUserPojoRs userRs = helper.createUser(testUser);

        AssertionsWithLog.assertEquals(userRs.job, userRs.job, "Поле job");
        AssertionsWithLog.assertEquals(userRs.name, userRs.name, "Поле name");

    }
}
