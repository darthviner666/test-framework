package com.framework.api.pojo.users.create.rs;

import com.framework.api.pojo.base.rs.BasePojoRs;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
/** * Класс для представления ответа на создание пользователя.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserPojoRs extends BasePojoRs {
    public String name;
    public String job;
    public String id;
    public Date createdAt;
}
