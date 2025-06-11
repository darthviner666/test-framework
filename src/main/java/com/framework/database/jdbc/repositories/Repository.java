package com.framework.database.jdbc.repositories;

import java.sql.SQLException;
import java.util.List;

/**
 * Абстрактный класс репозитория для работы с сущностями в базе данных.
 * Определяет основные CRUD операции, которые должны быть реализованы в конкретных репозиториях.
 *
 * @param <T> тип сущности, с которой работает репозиторий
 */
public abstract class Repository <T> {
    /** Имя таблицы в базе данных */
    protected String tableName;

    /**
     * Находит сущность по идентификатору.
     *
     * @param id идентификатор сущности
     * @return найденная сущность или null, если сущность не найдена
     */
    public abstract T findById(Long id);
    
    /**
     * Сохраняет новую сущность в базе данных.
     *
     * @param entity сущность для сохранения
     * @return сохраненная сущность с присвоенным идентификатором
     * @throws SQLException если произошла ошибка при сохранении
     */
    public abstract T save(T entity) throws SQLException;
    
    /**
     * Обновляет существующую сущность в базе данных.
     *
     * @param entity сущность для обновления
     * @return обновленная сущность
     */
    public abstract T update(T entity);
    
    /**
     * Удаляет сущность из базы данных.
     *
     * @param entity сущность для удаления
     */
    public abstract void delete(T entity);
    
    /**
     * Удаляет сущность по идентификатору.
     *
     * @param id идентификатор сущности для удаления
     */
    public abstract void deleteById(Long id);
    
    /**
     * Удаляет все сущности из таблицы.
     */
    public abstract void deleteAll();
    
    /**
     * Получает список всех сущностей из таблицы.
     *
     * @return список всех сущностей
     */
    public abstract List<T> findAll();
    
    /**
     * Создает таблицу в базе данных, если она не существует.
     */
    public abstract void createTableIfNotExists();
}
