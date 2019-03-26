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
        registry.addMapping("/**")
                .allowedMethods("*")
                // 远程地址(最好有所限制)
                .allowedOrigins("*")
                .allowedHeaders("*");
    }
}
