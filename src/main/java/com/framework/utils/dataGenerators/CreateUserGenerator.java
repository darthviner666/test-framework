package com.framework.utils.dataGenerators;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<CreateUserPojoRq> generateUsers(int count) {
        List<CreateUserPojoRq> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(generateUser());
        }
        return users;
    }

    public List<CreateUserPojoRq> generateUniqueUsers(int count) {
        Set<CreateUserPojoRq> usersSet = new HashSet<>();
        while (usersSet.size()<count){
            usersSet.add(generateUser());
        }
        return new ArrayList<>(usersSet);
    }

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
