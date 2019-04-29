package com.morningcx.ms.blog.base.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gcx
 * @date 2019/4/29
 */
public class LogUtil {
    /**
     * 解析日志描述
     *
     * @param s     日志描述
     * @param names 参数名称
     * @param args  参数引用
     * @return
     * @throws Exception
     */
    public static String parseDesc(String s, String[] names, Object[] args) throws Exception {
        int min = Math.min(names.length, args.length);
        Map<String, Object> map = new HashMap<>(min);
        for (int i = 0; i < min; ++i) {
            map.put(names[i], args[i]);
        }
        StringBuilder sb = new StringBuilder();
        char open = '{', close = '}';
        int start = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (open == s.charAt(i)) {
                int openIndex = i;
                sb.append(s, start, openIndex);
                while (s.charAt(++i) != close) ;
                // arg为{}中的内容，当前i为close的索引
                String arg = s.substring(openIndex + 1, i);
                sb.append(getRealValue(arg, map));
                start = i + 1;
            }
        }
        return sb.append(s, start, s.length()).toString();
    }

    private static String getRealValue(String s, Map<String, Object> map) throws Exception {
        if (s.contains(".")) {
            String[] args = s.split("\\.");
            Object obj = map.get(args[0]);
            Field field = obj.getClass().getDeclaredField(args[1]);
            field.setAccessible(true);
            return field.get(obj).toString();
        }
        Object o = map.get(s);
        return o == null ? "" : o.toString();
    }
}
