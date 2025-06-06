package com.db.jdbc;

import com.framework.database.jdbc.JdbcConnectManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class JdbcTestBase {
    JdbcConnectManager jdbcActions = JdbcConnectManager.getInstance();

    @BeforeMethod
    public void beforeMethod() {
    }

    @AfterMethod
    public void afterMethod() {
        jdbcActions.closeConnection();
    }
}
