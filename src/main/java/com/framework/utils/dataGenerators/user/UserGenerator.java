package com.framework.utils.dataGenerators.user;

import com.framework.database.tables.User;
import com.framework.utils.dataGenerators.faker.CustomFaker;
import com.framework.utils.dataGenerators.IGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserGenerator implements IGenerator<User> {
    CustomFaker faker = new CustomFaker();

    @Override
    public User generate() {
        return User.builder()
                .name(faker.name().fullName())
                .job(faker.job().title())
                .build();
    }

    @Override
    public List<User> generate(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(generate());
        }
        return users;
    }

    @Override
    public List<User> generateUnique(int count) {
        Set<User> users =  new HashSet<>();
        while (users.size() < count) {
            users.add(generate());
        }
        return new ArrayList<>(users);
    }
}
