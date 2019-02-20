package com.morningcx.ms.blog.base.result;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.morningcx.ms.blog.entity.enums.StateEnum;

import java.lang.reflect.Type;

/**
 * @author guochenxiao
 * @date 2019/2/20
 */
public class EnumJSONParser implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        int i = 0;
        String s = parser.parseObject(String.class);
        if (s.equals("高中")){
            return (T) StateEnum.HIGH;
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
