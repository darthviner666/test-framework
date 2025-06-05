package com.framework.api.pojo.users.create.rq;

import com.framework.api.pojo.base.rq.BasePojoRq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** * Класс для создания пользователя.
 * Содержит поля, необходимые для создания пользователя, такие как имя и должность.
 */
@Entity
@Table(name = "create_users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserPojoRq extends BasePojoRq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String name;

    @Column(nullable = false, unique = true)
    public String job;
}
