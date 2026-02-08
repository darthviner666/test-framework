package com.system.api;

import com.framework.api.helpers.ResourceHelper;
import com.framework.api.pojo.resource.Resource;
import com.framework.asserts.AssertionsWithLog;
import com.framework.utils.logger.TestLogger;
import com.testBase.TestBase;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Epic("API Тесты")
@Feature("Работа с ресурсами")
@Severity(SeverityLevel.BLOCKER)
public class GetResourcesOkTest extends TestBase {
    private final TestLogger LOGGER = new TestLogger(GetResourcesWithoutPojoOkTest.class);

    @DataProvider(name = "data", parallel = true)
    public Integer[][] provideData() {
        return new Integer[][]{
                {1},
                {2}
        };
    }

    @Test(description = "Проверка получения ресурсов",
            testName = "Получить ресурсы",
            groups = "smoke",
            dataProvider = "data",
            priority = 1)
    @Story("Положительный сценарий")
    @Severity(SeverityLevel.BLOCKER)
    public void getResourcesOkTest(Integer page) {
        LOGGER.info("Получение пользователей, страница: {}", page);
        Map<String, String> queryParams = new HashMap<>() {{
            put("page", page.toString());
        }};

        File jsonShema = new File("src/test/resources/schemas/resourceSchema.json");

        ResourceHelper helper = new ResourceHelper();
        Resource[] resources = helper.getResources(page, jsonShema);

        AssertionsWithLog.assertEquals(resources.length, 6, "Проверка размера массива ресурсов");

    }
}
