package com.morningcx.ms.blog.base.conf;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * EnableCaching开启注解支持
 *
 * @author gcx
 * @date 2019/3/15
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    /**
     * 注解的正反序列化和其他配置
     *
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jacksonSerializer = new GenericJackson2JsonRedisSerializer();
        // 解决查询缓存转换异常的问题
        /*ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);*/

        // 配置序列化（解决乱码的问题）,过期时间5分钟，这里返回新对象，必须链式或者每次重新获取
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonSerializer))
                .disableCachingNullValues();
        return RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }

    /**
     * 非注解的正反序列化和其他配置
     * 存储对象类型
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        // Jackson2JsonRedisSerializer可能存在泛型反序列化需要指定class的情况
        // 但是spring2.0 redis json中好像也带了class信息，没有报错
        /*// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围这里是ALL，ANY是包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化的类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);*/

        // generic比较通用，把一切对象变成Object，所以Long强转可能会传一些错误，并且json会带@class信息
        GenericJackson2JsonRedisSerializer jacksonSerializer = new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(stringRedisSerializer);
        // 值采用json序列化
        template.setValueSerializer(jacksonSerializer);

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jacksonSerializer);

        template.afterPropertiesSet();

        return template;
    }

    /**
     * 存储字符串类型
     *
     * @param factory
     * @return
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }

    /**
     * key 生成策略
     * 一定要继承CachingConfigurerSupport并重写才能全局配置
     * 不然需在@Cacheable注解中指定keyGenerator单个配置
     *
     * @return
     */
    @Override
    public KeyGenerator keyGenerator() {
        // 冒号可以用于分隔
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName()).append(":")
                    .append(method.getName()).append(":");
            for (Object obj : params) {
                sb.append(String.valueOf(obj));
            }
            return sb.toString();
        };
    }
}
