package com.Engulf.boot.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @RequestMapping("/kindred.jpg")
    public String find(){
        return "Test";
    }
//    rest
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String getUser(){
        return "GTE-Kindred";
    }
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String saveUser(){
        return "POST-Kindred";
    }
//    rest
    @PutMapping("/user")
    public String putUser(){
        return "PUT-Kindred";
    }
    @DeleteMapping("/user")
    public String deleteUser(){
        return "DELETE-Kindred";
    }
}
