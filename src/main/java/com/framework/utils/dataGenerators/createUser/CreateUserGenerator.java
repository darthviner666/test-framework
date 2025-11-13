package com.framework.utils.dataGenerators.createUser;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.utils.dataGenerators.faker.CustomFaker;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * Сгенерировать пользователей.
     * @param count - количество.
     * @return - список пользователей.
     */
    public List<CreateUserPojoRq> generateUsers(int count) {
        List<CreateUserPojoRq> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(generateUser());
        }
        return users;
    }

    /**
     * Сгенерировать уникальных пользователей.
     * @param count - количество.
     * @return - список уникальных пользователей.
     */
    public List<CreateUserPojoRq> generateUniqueUsers(int count) {
        return generateUniqueUsers(count, new ArrayList<>());
    }

    /**
     * Сгенерировать лист уникальных пользователей.
     * @param count - количество.
     * @param existingUsers - уже существующие пользователи.
     * @return - список уникальных пользователей.
     */
    public List<CreateUserPojoRq> generateUniqueUsers(int count, List<CreateUserPojoRq> existingUsers) {
        CreateUserPojoRq user;
        Set<CreateUserPojoRq> usersSet = new HashSet<>();
        while (usersSet.size() < count) {
            boolean uniqueUser = true;
            do {
                user = generateUser();
                for (CreateUserPojoRq existingUser : existingUsers) {
                    if (existingUser.name.equals(user.name) || existingUser.job.equals(user.job)) {
                        uniqueUser = false;
                        break;
                    }
                    uniqueUser = true;
                }
            } while (!uniqueUser);
            usersSet.add(user);
        }
        return new ArrayList<>(usersSet);
    }
}
