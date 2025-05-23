package com;

import com.framework.api.restAssured.ApiSpecs;
import com.framework.config.ConfigReader;
import com.framework.config.ProjectConfig;
import io.restassured.RestAssured;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Класс для базовой настройки автотестов.
 */
public class TestBase {
    private static final ProjectConfig config = ConfigReader.Instance();

    /**
     * Настройка.
     */
    @BeforeSuite
    static void setup() {

        // RestAssured configuration
        //RestAssured.baseURI = config.apiBaseUrl();
        RestAssured.requestSpecification = ApiSpecs.getDefaultRequestSpec();
        RestAssured.responseSpecification = ApiSpecs.getDefaultResponseSpec();

    }

    /**
     * Выход из теста.
     */
    @AfterMethod
    void tearDown() {
        RestAssured.reset();
    }
}
