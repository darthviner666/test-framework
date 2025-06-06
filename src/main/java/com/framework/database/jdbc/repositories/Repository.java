package com.framework.database.jdbc.repositories;

import java.sql.SQLException;
import java.util.List;

public abstract class Repository <T> {
    protected String tableName;

    public abstract T findById(Long id);
    public abstract T save(T entity) throws SQLException;
    public abstract T update(T entity);
    public abstract void delete(T entity);
    public abstract void deleteById(Long id);
    public abstract void deleteAll();
    public abstract List<T> findAll();
}
