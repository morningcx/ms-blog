package com.morningcx.ms.blog.controller.admin;

import com.morningcx.ms.blog.base.annotation.FreeAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author gcx
 * @date 2019/5/13
 */
@FreeAuth
@Controller
public class IndexController {
    @Value("${profile.active}")
    private String profile;

    @GetMapping("/")
    public String index() {
        return "redirect:/login/login.html";
    }

    @GetMapping("/profile")
    @ResponseBody
    public String profile() {
        return profile;
    }
}
