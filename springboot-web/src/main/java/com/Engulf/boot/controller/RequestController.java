package com.Engulf.boot.controller;

//<li>@RequestAttribute(获取request域属性)</li>

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RequestController {
    @GetMapping("/goto")
    public String goToPage(HttpServletRequest request){
        request.setAttribute("msg","gnardada~");
        request.setAttribute("code",200);
        return "forward:/success";
    }

    @ResponseBody
    @GetMapping("/success")
    public Map success(@RequestAttribute("msg") String msg, @RequestAttribute("code") Integer code,
                       HttpServletRequest request){
        Object msg1 = request.getAttribute("msg");
        Map<String,Object> map = new HashMap<>();
        map.put("anno_msg",msg);
        map.put("anno_code",code);
        map.put("req_msg2",msg1);
        return map;
    }

    @GetMapping("/demo.html")
    public String demo(){
        return "demo";
    }
}
