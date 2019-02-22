package com.morningcx.ms.blog.base.other;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * fast json 枚举类反序列化解析，前端json可以直接传递中文值(@RequestBody)
 * 实现枚举实例含有EnumDesc注解的反向绑定，更好的配合了mybatis-plus
 *
 * @author guochenxiao
 * @date 2019/2/20
 */
public class EnumJSONParser implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String s = parser.parseObject(String.class);
        try {
            Class<?> targetClass = Class.forName(type.getTypeName());
            String desc = null;
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(EnumDesc.class)) {
                    desc = field.getName();
                    break;
                }
            }
            Method method = targetClass.getMethod("get" + desc.substring(0, 1).toUpperCase() + desc.substring(1));
            Object[] objects = targetClass.getEnumConstants();
            for (Object obj : objects) {
                if (s.equals(method.invoke(obj))) {
                    return (T) obj;
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
