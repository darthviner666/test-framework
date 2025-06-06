package com.framework.utils.dataGenerators;

import com.github.javafaker.Faker;
import com.github.javafaker.PhoneNumber;

import java.util.Random;

/**
 * Класс для генерации данных.
 */
public class CustomFaker extends Faker {
//TODO сохранять в базу с названием теста
    /**
     * Получить email.
     * @return - email.
     */
    public static String email() {
        String email = Faker.instance().name().firstName().toLowerCase() + "." + Faker.instance().name().lastName().toLowerCase() + "@email.com";
        return email;
    }
    /**
     * Получить пароль.
     * @return - пароль.
     */
    public static String password() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    /**
     * Получить номер телефон без первой цифры.
     * @return - номер телефона.
     */
    public static String getPhoneNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(random.nextInt(9));
        }
        return stringBuilder.toString();
    }

    /**
     * Случайное булево.
     * @return - булево.
     */
    public static Boolean randomBoolean() {
        return new Random().nextBoolean();
    }

}
