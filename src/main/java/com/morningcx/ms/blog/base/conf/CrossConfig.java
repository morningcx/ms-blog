package com.morningcx.ms.blog.base.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author gcx
 * @date 2019/3/25
 */
@Configuration
public class CrossConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 对前端接口实现跨域
        registry.addMapping("/web/**")
                .allowedMethods("*")
                // todo 远程地址(本地)
                .allowedOrigins("*")
                .allowedHeaders("*");
    }
}
