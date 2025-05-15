package com.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTest {
    @Test
    @DisplayName("Get user by id")
    void getUserByIdTest() {
        given()
                .log().all()
                .when()
                .get("/users/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", equalTo(1));
    }
}
