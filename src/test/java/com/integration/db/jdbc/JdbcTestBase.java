package com.integration.db.jdbc;

import com.framework.database.jdbc.JdbcConnectManager;
import com.testBase.TestBase;
import org.testng.annotations.AfterMethod;

public class JdbcTestBase extends TestBase {
    JdbcConnectManager jdbcActions = JdbcConnectManager.getInstance();


    @AfterMethod
    public void afterMethod() {
        jdbcActions.closeConnection();
    }
}
