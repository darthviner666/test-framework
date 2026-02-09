package com.framework.utils.dataGenerators.uiUser;

import com.framework.ui.pojo.UserUiPojo;
import com.framework.utils.dataGenerators.faker.CustomFaker;
import lombok.experimental.UtilityClass;
/** * Класс для генерации пользователей для UI.
 * Генерирует случайные данные для пользователя, такие как имя, email, пароль и т.д.
 */
@UtilityClass
public class UiUserGenerator {
    /** * Сгенерировать пользователя для UI.
     * @return - пользователь со случайными данными.
     */
    public UserUiPojo generateUser() {
        return UserUiPojo
                .builder()
                .name(CustomFaker.instance().name().firstName()  + System.currentTimeMillis())
                .email(CustomFaker.email())
                .password(CustomFaker.password())
                .phone(CustomFaker.getPhoneNumber())
                .haveChildren(CustomFaker.randomBoolean())
                .havePregnancy(CustomFaker.randomBoolean())
                .build();
    }

    /** * Сгенерировать пользователя с невалидным email для UI.
     * @return - пользователь со случайными данными и невалидным email.
     */
    public UserUiPojo generateInvalidEmailUser() {
        return UserUiPojo
                .builder()
                .name(CustomFaker.instance().name().firstName() + System.currentTimeMillis())
                .email(CustomFaker.email())
                .password(CustomFaker.password())
                .phone(CustomFaker.getPhoneNumber())
                .haveChildren(CustomFaker.randomBoolean())
                .havePregnancy(CustomFaker.randomBoolean())
                .build();
    }
}
