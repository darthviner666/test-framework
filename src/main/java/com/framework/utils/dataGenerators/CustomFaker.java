package com.framework.utils.dataGenerators;

import com.github.javafaker.Faker;

import java.util.List;
import java.util.Random;

/**
 * Класс для генерации данных.
 */
public class CustomFaker extends Faker {
    //TODO заменить на SQl
    List<String> jobs = List.of("job1","job2","job3","job4");

    /**
     * Получить работу.
     * @return - название работы.
     */
    public String getJob() {
        return jobs.get(new Random().nextInt(jobs.size()));
    }
}
