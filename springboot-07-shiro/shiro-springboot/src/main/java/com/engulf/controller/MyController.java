package com.engulf.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {
    @RequestMapping({"/index","/"})
    public String toIndex(Model model){
        model.addAttribute("msg","kindred");
        return "index";
    }

    @RequestMapping("/champion/add")
    public String addChampion(){
        return "champion/addChampion";
    }

    @RequestMapping("/champion/update")
    public String updateChampion(){
        return "champion/updateChampion";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username,String password,Model model){
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);  //执行登录的方法
            return "redirect:index";
        }catch (UnknownAccountException uae){
            model.addAttribute("msg","用户名错误");
            return "login";
        }catch (IncorrectCredentialsException ice) { //密码错误
            model.addAttribute("msg","密码错误");
            return "login";
        }
    }

    @RequestMapping("/Unauthorized")
    public String Unauthorized(Model model){
        model.addAttribute("msg","您没有访问的权限");
        return "unauthorized";
    }
}
