package com.framework.api.dataGenerators;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

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
                .job(faker.getJob())
                .name(faker.name().firstName())
                .build();
    }
}
