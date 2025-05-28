package com.framework.api.pojo.users.create.rq;

import com.framework.api.pojo.base.rq.BasePojoRq;
import lombok.Builder;
import lombok.Data;
/** * Класс для создания пользователя.
 * Содержит поля, необходимые для создания пользователя, такие как имя и должность.
 */
@Builder
public class CreateUserPojoRq extends BasePojoRq {
    public String name;
    public String job;
}
