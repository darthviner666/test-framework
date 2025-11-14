package com.framework.ui.pom.elements.anotations;

import com.codeborne.selenide.SelenideElement;
import com.framework.ui.pom.elements.Button;
import com.framework.ui.pom.elements.Checkbox;
import com.framework.ui.pom.elements.Form;
import com.framework.ui.pom.elements.Input;
import org.openqa.selenium.By;

import java.lang.reflect.Field;

public class UiElementProcessor {

    public static void initPageObject(Object pageObject) {
        Class<?> clazz = pageObject.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(UiElement.class)) {
                processCustomElement(pageObject, field);
            }
        }
    }

    private static void processCustomElement(Object pageObject, Field field) {
        field.setAccessible(true);
        UiElement annotation = field.getAnnotation(UiElement.class);

        try {
            By locator = createLocator(annotation);
            SelenideElement selenideElement = com.codeborne.selenide.Selenide.$(locator);
            Object customElement = createCustomElement(field.getType(), selenideElement, annotation);
            field.set(pageObject, customElement);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize custom element: " + field.getName(), e);
        }
    }

    private static Object createCustomElement(Class<?> elementType, SelenideElement selenideElement, UiElement annotation) {
        String elementName = annotation.name().isEmpty() ?
                selenideElement.getSearchCriteria() : annotation.name();

        if (Input.class.isAssignableFrom(elementType)) {
            return new Input(selenideElement, elementName);
        } else if (Button.class.isAssignableFrom(elementType)) {
            return new Button(selenideElement, elementName);
        } else if (Checkbox.class.isAssignableFrom(elementType)) {
            return new Checkbox(selenideElement, elementName);
        } else if (Form.class.isAssignableFrom(elementType)) {
            return new Form(selenideElement, elementName);
        } else {
            throw new IllegalArgumentException("Unsupported element type: " + elementType);
        }
    }

    private static By createLocator(UiElement annotation) {
        if (!annotation.xpath().isEmpty()) {
            return By.xpath(annotation.xpath());
        } else if (!annotation.css().isEmpty()) {
            return By.cssSelector(annotation.css());
        } else {
            throw new IllegalArgumentException("Either xpath or css must be specified for UiElement");
        }
    }
}
