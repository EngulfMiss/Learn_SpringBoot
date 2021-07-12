package com.engulf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

//在template目录下的所有页面，只能通过请求访问
//需要模板引擎的支持  thymeleaf
@Controller
public class IndexController {
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping({"/","/index","/index.html"})
    public String index(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session){
        if((StringUtils.hasLength(username) && "Kindred".equals(username)) && "w2snowgnar".equals(password)){
            session.setAttribute("LoginUser",username);
            return "redirect:/main.html";
        }else {
            model.addAttribute("errorMsg","用户名或者密码错误");
        }
        return "login";
    }

    @GetMapping("/main.html")
    public String toMainPage(){
        return "index";
    }

    @GetMapping("/logout")
    public String Logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}
