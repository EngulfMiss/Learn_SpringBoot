package com.Engulf.boot.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//<li>@PathVariable(路径变量)</li>
//<li>@RequestHeader(获取请求头)</li>
//<li>@RequestParam(获取请求参数)</li>
//<li>@CookieValue(获取cookie值)</li>
//<li>@RequestAttribute(获取request域属性)</li>
//<li>@RequestBody[POST](获取请求体)</li>
//<li>@MatrixVariable(矩阵变量)</li>

@RestController
public class ParameterTestController {
    @GetMapping("/champion/{id}/friend/{username}")
    public Map<String,Object> getCar(@PathVariable("id") Integer id,
                                     @PathVariable("username") String username,
                                     @PathVariable Map<String,String> pv,
                                     @RequestHeader("User-Agent") String userAgent,
                                     @RequestHeader Map<String,String> header,
                                     @RequestParam("age") Integer Kindredage,
                                     @RequestParam("job") List<String> jobs,
                                     @RequestParam Map<String,String> param
                                     /*@CookieValue("_ga") String _cv,
                                     @CookieValue("_ga") Cookie cookie*/){
        Map<String,Object> map = new HashMap<>();
//        @PathVariable
        map.put("id",id);
        map.put("username",username);
        map.put("pv",pv);
//        @RequestHeader
//        map.put("userAgent",userAgent);
//        map.put("header",header);
//        @RequestParam
        map.put("age",Kindredage);
        map.put("job",jobs);
        map.put("params",param);
//        @CookieValue
//        map.put("cookieV",_cv);
//        map.put("cookie",cookie);
        return map;
    }

    @PostMapping("/save")
    public Map postMethod(@RequestBody String content){
        Map<String,Object> map = new HashMap<>();
        map.put("cont",content);
        return map;
    }

    //  1.语法  /cars/sell;low=34;brand=byd,audi,yd
    //  2.SpringBoot默认禁用矩阵变量功能
    //       手动启动：原理，对于路径的处理。UrlPathHelper进行解析。
    //       removeSemicolonContent(移出分号内容)支持矩阵变量的
    //  3.矩阵变量必须要有url路径变量才能被解析，{}
    @GetMapping("/cars/{url}")
    public Map carsSell(@MatrixVariable("low") Integer low,
                        @MatrixVariable("brand") List<String> brand,
                        @PathVariable("url") String url){
        Map<String,Object> map = new HashMap<>();
        map.put("lower",low);
        map.put("brands",brand);
        map.put("path",url);
        return map;
    }

    //   /boss/1;age=20/2;age=10
    @GetMapping("/boss/{bossId}/{empId}")
    public Map boss(@MatrixVariable(value = "age",pathVar = "bossId") Integer Bossage,
                    @MatrixVariable(value = "age",pathVar = "empId") Integer Empage){
        Map<String,Object> map = new HashMap<>();
        map.put("boss",Bossage);
        map.put("emp",Empage);
        return map;
    }
}
