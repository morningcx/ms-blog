package com.morningcx.ms.blog;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@MapperScan("com.morningcx.ms.blog.mapper")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Controller
public class MsBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsBlogApplication.class, args);
        log.info("========================启动成功========================");
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/show/index.html";
    }
}

