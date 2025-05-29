package com.framework.utils.dataGenerators;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import lombok.experimental.UtilityClass;
/**
 *  * Класс для генерации пользователей для создания.
 */
@UtilityClass
public class CreateUserGenerator {
    CustomFaker faker = new CustomFaker();

    /**
     * Сгенерировать пользователя для создания.
     * @return - полльзователь.
     */
    public CreateUserPojoRq generateUser() {
        return CreateUserPojoRq
                .builder()
                .job(faker.job().title())
                .name(faker.name().firstName())
                .build();
    }
}
