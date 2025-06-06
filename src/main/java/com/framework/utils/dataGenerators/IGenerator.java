package com.framework.utils.dataGenerators;

import java.util.List;

public interface IGenerator <T>{
     default T generate() {
        return null;
    }
    default List<T> generate(int count) {
        return null;
    }
    default List<T> generateUnique(int count) {
        return null;
    }
}
