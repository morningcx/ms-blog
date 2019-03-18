package com.morningcx.ms.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest

public class MsBlogApplicationTests {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Test
    public void contextLoads() {
        redisTemplate.opsForValue().set("springboot2", "hello");
        System.out.println(redisTemplate.opsForValue().get("springboot"));

    }

    @Test
    public void simpleMailTest() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom); // 发送人的邮箱要和user name一致
        message.setSubject("测试"); //标题
        message.setTo("877690699@qq.com"); //发给谁  对方邮箱
        message.setText("1"); //内容
        mailSender.send(message); //发送
    }

}

