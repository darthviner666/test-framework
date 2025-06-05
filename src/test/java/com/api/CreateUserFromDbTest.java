package com.api;

import com.framework.api.helpers.UserHelper;
import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.api.pojo.users.create.rs.CreateUserPojoRs;
import com.framework.asserts.AssertionsWithAllureLog;
import com.framework.database.DatabaseConfig;
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
    @BeforeMethod
    void prepareTestUser() {
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            testUser = session.createQuery("from User order by RANDOM() limit 1", CreateUserPojoRq.class)
                    .getSingleResult();
            Transaction tx = session.beginTransaction();
            session.remove(testUser);
            tx.commit();
        }
    }
    /**
     * Тест для проверки создания пользователей.
     */
    @Test(description = "Проверка создания пользователей из базы",
            groups = "smoke",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void createUserFromDbTest() {
        UserHelper helper = new UserHelper();
        CreateUserPojoRs userRs = helper.createUser(testUser);

        AssertionsWithAllureLog.assertEquals(userRs.job, userRs.job, "Поле job");
        AssertionsWithAllureLog.assertEquals(userRs.name, userRs.name, "Поле name");

    }
}
