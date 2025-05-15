package com.api;

import com.TestBase;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

import static io.restassured.RestAssured.given;

@Feature("Get Users List")
public class GetListUsersTest extends TestBase {
    @DisplayName("Get Users List Test OK")
    public void getUsersListOkTest() {
        given()
                .get()

    }
}
