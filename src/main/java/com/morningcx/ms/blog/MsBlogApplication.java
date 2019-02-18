package com.morningcx.ms.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@Slf4j
@SpringBootApplication
public class MsBlogApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MsBlogApplication.class, args);
        log.info("========================启动成功========================");
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

