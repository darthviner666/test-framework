package com.framework.utils.dataGenerators.user;

import com.framework.database.tables.User;
import com.framework.utils.dataGenerators.faker.CustomFaker;
import com.framework.utils.dataGenerators.IGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Генератор тестовых данных для сущности User.
 * Реализует интерфейс IGenerator для создания случайных пользователей
 * с использованием библиотеки Faker.
 */
public class UserGenerator implements IGenerator<User> {
    /** Экземпляр CustomFaker для генерации случайных данных */
    CustomFaker faker = new CustomFaker();

    /**
     * Генерирует одного случайного пользователя.
     * 
     * @return новый объект User со случайными данными
     */
    @Override
    public User generate() {
        return User.builder()
                .name(faker.name().fullName())
                .job(faker.job().title())
                .build();
    }

    /**
     * Генерирует указанное количество случайных пользователей.
     * 
     * @param count количество пользователей для генерации
     * @return список объектов User со случайными данными
     */
    @Override
    public List<User> generate(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(generate());
        }
        return users;
    }

    /**
     * Генерирует указанное количество уникальных пользователей.
     * Использует HashSet для обеспечения уникальности сгенерированных объектов.
     * 
     * @param count количество уникальных пользователей для генерации
     * @return список уникальных объектов User со случайными данными
     */
    @Override
    public List<User> generateUnique(int count) {
        Set<User> users =  new HashSet<>();
        while (users.size() < count) {
            users.add(generate());
        }
        return new ArrayList<>(users);
    }
}
