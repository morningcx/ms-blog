package com.morningcx.ms.blog;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@MapperScan("com.morningcx.ms.blog.mapper")
@SpringBootApplication
public class MsBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsBlogApplication.class, args);
        log.info("========================启动成功========================");
    }
}

