package com.framework.utils.dataGenerators;

import java.util.List;

/**
 * Интерфейс для генераторов тестовых данных.
 * Определяет методы для создания одиночных и множественных объектов.
 *
 * @param <T> тип генерируемых объектов
 */
public interface IGenerator <T>{
    /**
     * Генерирует один объект с тестовыми данными.
     *
     * @return новый объект типа T
     */
    default T generate() {
        return null;
    }
    
    /**
     * Генерирует указанное количество объектов с тестовыми данными.
     *
     * @param count количество объектов для генерации
     * @return список объектов типа T
     */
    default List<T> generate(int count) {
        return null;
    }
    
    /**
     * Генерирует указанное количество уникальных объектов с тестовыми данными.
     *
     * @param count количество уникальных объектов для генерации
     * @return список уникальных объектов типа T
     */
    default List<T> generateUnique(int count) {
        return null;
    }
}
