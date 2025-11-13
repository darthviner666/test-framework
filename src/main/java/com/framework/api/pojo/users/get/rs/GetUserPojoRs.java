package com.framework.api.pojo.users.get.rs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.framework.api.pojo.base.rs.BasePojoRs;

import lombok.Data;

/**
 * POJO класс для получения пользователей.
 */
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
