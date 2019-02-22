package com.morningcx.ms.blog.base.util;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.morningcx.ms.blog.entity.Article;

import java.lang.reflect.Field;

/**
 * @author guochenxiao
 * @date 2019/2/21
 */
public class MybatisResultUtil {
    public static String getResultsStr(Class origin) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("@Results({\n");
        for (Field field : origin.getDeclaredFields()) {
            String property = field.getName();
            //映射关系：对象属性(驼峰)->数据库字段(下划线)
            String column = new PropertyNamingStrategy.SnakeCaseStrategy().translate(field.getName());
            stringBuilder.append(String.format("@Result(property = \"%s\", column = \"%s\"),\n", property, column));
        }
        stringBuilder.append("})");
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(getResultsStr(Article.class));
    }
}
