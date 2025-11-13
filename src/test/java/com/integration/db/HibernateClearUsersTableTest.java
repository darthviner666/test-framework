package com.integration.db;

import com.framework.asserts.AssertionsWithLog;
import com.framework.database.hibernate.DatabaseHibernateActions;
import com.testBase.TestBase;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

public class HibernateClearUsersTableTest extends TestBase {

    @Test(description = "Проверка очистки таблицы create_users",
            groups = "smoke",
            priority = 1)
    @Story("Положительный сценарий")
    public void hibernateClearUsersTableTest() {
        DatabaseHibernateActions.clearUsersTable();
        int usersCount = DatabaseHibernateActions.getUsersCount();
        AssertionsWithLog.assertEquals(usersCount, 0, "Таблица create_users очищена");
    }
}
