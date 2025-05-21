package com.framework.api.pojo.users.get.rs;

import com.framework.api.pojo.base.rs.BasePojoRs;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * POJO класс для получения пользователей.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserPojoRs extends BasePojoRs {
    public Long id;
    public String email;
    @JsonProperty("first_name")
    public String firstName;
    @JsonProperty("last_name")
    public String lastName;
    public String avatar;
}
