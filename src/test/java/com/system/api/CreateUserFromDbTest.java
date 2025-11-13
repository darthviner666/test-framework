package com.system.api;

import com.framework.api.helpers.UserHelper;
import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.api.pojo.users.create.rs.CreateUserPojoRs;
import com.framework.asserts.AssertionsWithLog;
import com.framework.database.hibernate.DatabaseHibernateActions;
import com.framework.database.hibernate.DatabaseHibernateHikariConfig;
import com.testBase.TestBase;
import io.qameta.allure.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.*;

/**
 * Тест по созданию пользователей из базы данных.
 */
@Epic("API Тесты")
@Feature("Работа с пользователями")
@Severity(SeverityLevel.BLOCKER)
public class CreateUserFromDbTest extends TestBase {

    @BeforeClass
    private void dbSetup() {
        try {
            DatabaseHibernateHikariConfig.initDatabase();
            DatabaseHibernateActions.ensureUsersExist(20);
        } catch (Exception e) {
            log.error("Error during setup", e);
            throw e;
        }
    }

    @AfterClass
    private void dbShutdown() {
        DatabaseHibernateHikariConfig.shutdown();
    }

    @DataProvider(name = "data", parallel = true)
    CreateUserPojoRq[][] data() {
        return new CreateUserPojoRq[][] {
                {DatabaseHibernateActions.getTestUser()},
                {DatabaseHibernateActions.getTestUser()}
        };
    }

    /**
     * Тест для проверки создания пользователей, которых мы берём из тестовой базы.
     */
    @Test(description = "Проверка создания пользователей взятого из базы с помощью Hibernate",
            groups = "smoke",
            priority = 1,
    dataProvider = "data")
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void createUserFromDbTest(CreateUserPojoRq testUser) {
        UserHelper helper = new UserHelper();
        CreateUserPojoRs userRs = helper.createUser(testUser);

        AssertionsWithLog.assertEquals(userRs.job, userRs.job, "Поле job");
        AssertionsWithLog.assertEquals(userRs.name, userRs.name, "Поле name");

    }
}
