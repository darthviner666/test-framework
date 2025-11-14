package com.framework.ui.pom.elements.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UiElement {
    String xpath() default "";
    String css() default "";
    String name() default ""; // человеко-читаемое имя элемента
    int timeout() default 10; // таймаут в секундах
}
