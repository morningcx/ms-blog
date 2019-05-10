package com.morningcx.ms.blog;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@Controller
@MapperScan("com.morningcx.ms.blog.mapper")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MsBlogApplication {
    @Value("${profile.active}")
    private String profile;

    public static void main(String[] args) {
        SpringApplication.run(MsBlogApplication.class, args);
        log.info("========================启动成功========================");
    }

    @GetMapping("/")
    public String index() {
        /*return "redirect:/show/index.html";*/
        return "redirect:/login/login.html";
    }

    @GetMapping("/profile")
    @ResponseBody
    public String profile() {
        return profile;
    }
}

