package com.framework.database.tables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность пользователя для хранения в базе данных.
 * Используется как для Hibernate, так и для JDBC операций.
 */
@Entity
@Table(name = "USERS")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    /** Уникальный идентификатор пользователя, автоматически генерируется */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Имя пользователя, должно быть уникальным */
    @Column(nullable = false, unique = true)
    private String name;

    /** Должность пользователя, должна быть уникальной */
    @Column(nullable = false, unique = true)
    private String job;
}
