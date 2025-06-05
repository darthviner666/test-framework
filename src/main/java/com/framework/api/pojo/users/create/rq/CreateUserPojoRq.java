package com.framework.api.pojo.users.create.rq;

import com.framework.api.pojo.base.rq.BasePojoRq;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
/** * Класс для создания пользователя.
 * Содержит поля, необходимые для создания пользователя, такие как имя и должность.
 */
@Entity
@Table(name = "create_users")
@Builder
public class CreateUserPojoRq extends BasePojoRq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public String job;
}
