package com.morningcx.ms.blog.base.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

/**
 * @author gcx
 * @date 2019/2/26
 */
public class EntityUtil {

    /**
     * 对实体类中string类型的实例域进行trim操作(插入操作常用)
     *
     * @param object
     */
    @SneakyThrows
    public static void trim(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                String value = (String) field.get(object);
                if (value != null) {
                    field.set(object, value.trim());
                }
            }
        }
    }

    /**
     * 移除实体类中为空的字符串，避免出现like '%%' (查询操作常用)
     *
     * @param t
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> T removeEmptyString(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                String value = (String) field.get(t);
                // 字符串为空则置null
                if (value != null && "".equals(value.trim())) {
                    field.set(t, null);
                }
            }
        }
        return t;
    }


}
