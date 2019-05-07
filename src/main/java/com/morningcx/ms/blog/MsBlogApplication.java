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
        return "/show/index.html";
    }
    /**
     * fast json
     *
     * @return
     */
    /*@Bean
    public HttpMessageConverters fastJsonConfig() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        // 日期格式化，需要PrettyFormat支持
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        converter.setFastJsonConfig(fastJsonConfig);
        // 解决返回json中文乱码
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        return new HttpMessageConverters(converter);
    }*/

}

