package com.gnardada.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@Controller
//@ResponseBody //使用@ResponseBody修饰控制器类，返回的字符串不再是找页面，而是返回字符串

@RestController  //@ResponseBody和@Controller的组合
@RequestMapping("/Hello")
public class HelloController {
    @RequestMapping("/World")
    public String handle01(){
        return "Hello,Spring Boot 2!"+"你好";
    }
}
