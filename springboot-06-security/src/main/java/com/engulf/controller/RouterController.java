package com.engulf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SuppressWarnings("all")
public class RouterController {
    @RequestMapping({"/","/index","/index.html"})
    public String index(){
        return "index";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "views/login";
    }

    @RequestMapping("/{model}/{id}")
    public String level1(@PathVariable("model") String model,@PathVariable("id") int id){
        return "views/"+model+"/"+id;
    }


//    @RequestMapping("/level2/{id}")
//    public String level2(@PathVariable("id") int id){
//        return "views/level2/"+id;
//    }
//
//    @RequestMapping("/level3/{id}")
//    public String level3(@PathVariable("id") int id){
//        return "views/level3/"+id;
//    }
}
