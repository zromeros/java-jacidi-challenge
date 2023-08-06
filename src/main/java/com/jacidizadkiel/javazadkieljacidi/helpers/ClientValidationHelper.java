package com.jacidizadkiel.javazadkieljacidi.helpers;

import java.lang.reflect.Field;

public class ClientValidationHelper {

    public static boolean validateRequiredFields(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(object);
                
                boolean validation = value == null || (value instanceof String && ((String) value).isEmpty());
                return validation;
            } catch (IllegalAccessException e) {
                return false;
            }
        }

        return true;
    }
}