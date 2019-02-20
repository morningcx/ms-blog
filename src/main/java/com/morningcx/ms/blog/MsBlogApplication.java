package com.morningcx.ms.blog;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@Slf4j
@MapperScan("com.morningcx.ms.blog.mapper")
@SpringBootApplication
public class MsBlogApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MsBlogApplication.class, args);
        log.info("========================启动成功========================");
    }

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * mybatis-plus逻辑删除
     * @return
     */
    @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
    }

    /**
     * fast json
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonConfig(){
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        /*fastJsonConfig.setSerializerFeatures(
                // 日期格式化
                SerializerFeature.PrettyFormat
                // 枚举类使用toString
                *//*SerializerFeature.WriteEnumUsingToString,*//*
                *//*SerializerFeature.WriteEnumUsingName*//*
        );*/
        // 日期格式化，需要PrettyFormat支持
        /*fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");*/
        converter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(converter);
    }

    /**
     * 打成war包时需要配置继承SpringBootServletInitializer，相当于一个web.xml
     * jar包不需要
     *
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MsBlogApplication.class);
    }
}

