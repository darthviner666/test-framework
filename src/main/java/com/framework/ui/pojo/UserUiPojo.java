package com.framework.ui.pojo;

import lombok.Builder;

@Builder
public class UserUiPojo {
    public String name;
    public String email;
    public String password;
    public String phone;
    public Boolean haveChildren;
    public Boolean havePregnancy;
}
