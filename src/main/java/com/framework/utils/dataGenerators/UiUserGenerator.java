package com.framework.utils.dataGenerators;

import com.framework.ui.pojo.UserUiPojo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UiUserGenerator {
    public UserUiPojo generateUser() {
        return UserUiPojo
                .builder()
                .name(CustomFaker.instance().name().firstName())
                .email(CustomFaker.email())
                .password(CustomFaker.password())
                .phone(CustomFaker.getPhoneNumber())
                .haveChildren(CustomFaker.randomBoolean())
                .havePregnancy(CustomFaker.randomBoolean())
                .build();
    }
}
