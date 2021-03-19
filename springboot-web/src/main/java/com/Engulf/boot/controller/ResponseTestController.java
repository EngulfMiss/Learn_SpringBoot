package com.Engulf.boot.controller;

import com.Engulf.boot.domain.Champion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ResponseTestController {
    @ResponseBody
    @GetMapping("/test/champion")
    public Champion getChampion(){
        Champion champion = new Champion();
        champion.setName("Kindred");
        champion.setAge(1500);
        champion.setBirthday(new Date());
        return champion;
    }
}
