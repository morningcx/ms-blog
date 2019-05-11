package com.morningcx.ms.blog.base.util;

import com.morningcx.ms.blog.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author gcx
 * @date 2019/5/11
 */
@Component
public class ConfigUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ConfigMapper configMapper;

    /**
     * 获取系统配置，过期时间为1小时
     *
     * @param key
     * @return
     */
    public String getConfig(String key) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String s = ops.get("system:config:" + key);
        if (s == null) {
            s = configMapper.getValue(key);
            ops.set("system:config:" + key, s, Duration.ofHours(1));
        }
        return s;
    }
}
