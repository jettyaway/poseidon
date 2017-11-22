package com.voxlearning.poseidon.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnnoatationsUtil {
    public AnnoatationsUtil() {
    }

    public static String getFieldNameByFieldAnnoatation(Class clazz, Class<? extends Annotation> annon) {
        String fieldName = null;
        Field[] fields = clazz.getDeclaredFields();
        Field[] var4 = fields;
        int var5 = fields.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            if (field.isAnnotationPresent(annon)) {
                fieldName = field.getName();
                break;
            }
        }

        return fieldName;
    }

    public static <T> Object getFieldValueByFieldAnnonation(T model, Class<? extends Annotation> annon) {
        String fieldName = getFieldNameByFieldAnnoatation(model.getClass(), annon);
        return getFieldValueByFieldName(model, fieldName);
    }

    public static <T> Object getFieldValueByFieldName(T model, String fieldName) {
        return JackBeanUtil.getProperty(model, fieldName);
    }

    public static List<Field> getFieldsBySpecAnnoatation(Class clazz, Class<? extends Annotation> annon) {
        List<Field> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        Field[] var4 = fields;
        int var5 = fields.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            if (field.isAnnotationPresent(annon)) {
                list.add(field);
            }
        }

        return list;
    }

    public static List<String> getFieldsBySpecAnnoatationWithString(Class clazz, Class<? extends Annotation> annon) {
        List<Field> fields = getFieldsBySpecAnnoatation(clazz, annon);
        List<String> results = new ArrayList<>();

        for (Field field : fields) {
            results.add(field.getName());
        }

        return results;
    }

    public static void TT(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Field[] var2 = fields;
        int var3 = fields.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            System.out.println(field.getName());
        }

    }

    public static void main(String[] args) {
    }
}