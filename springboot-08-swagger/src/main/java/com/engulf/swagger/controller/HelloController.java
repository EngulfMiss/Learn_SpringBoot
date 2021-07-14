package com.engulf.swagger.controller;

import com.engulf.swagger.pojo.Champion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@Api(tags = "名为Hello的控制器类")
@RestController
public class HelloController {
    @GetMapping(value = "/hello")
    public String hello(){
        return "Hello World";
    }

    //只要我们的接口的返回值中存在实体类，就会被swagger扫描到
    @GetMapping("/champion")
    public Champion champion(){
        return new Champion();
    }

    @GetMapping("/opt/{name}")
    @ApiOperation("名为operation的方法")  //给控制器方法的注释
    public String operation(@PathVariable("name") @ApiParam("用户名") String username){
        return "Hello" + username;
    }

    @PostMapping("/user/{username}/{password}")
    @ApiOperation("获取用户实体类的方法")
    public String getUser(@PathVariable("username") @ApiParam("用户名") String username,@PathVariable("password") @ApiParam("密码") String password){
        return new Champion(username,password).toString();
    }
}
