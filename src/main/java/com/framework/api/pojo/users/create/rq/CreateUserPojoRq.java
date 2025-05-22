package com.framework.api.pojo.users.create.rq;

import com.framework.api.pojo.base.rq.BasePojoRq;
import lombok.Builder;
import lombok.Data;

@Builder
public class CreateUserPojoRq extends BasePojoRq {
    public String name;
    public String job;
}
