package com.framework.utils.dataGenerators.faker;

import com.github.javafaker.Faker;

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
            int digit = random.nextInt(9);
            //Первая цифра номера не должна быть 7 или 8
            if (i==0) {
                while (digit == 7 || digit == 8) {
                    digit = random.nextInt(9);
                }
            }
            stringBuilder.append(digit);
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
