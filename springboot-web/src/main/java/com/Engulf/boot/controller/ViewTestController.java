package com.Engulf.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Slf4j
@Controller
public class ViewTestController {
/*    @GetMapping("/sayHello")
    public String sayHello(Model model){
        //model中的数据会被放在请求域中
        model.addAttribute("msg","Hello World!");
        model.addAttribute("link","https://www.baidu.com");
        return "HW";
    }*/

    @GetMapping("/test")
    public String sayHello(Model model){
        //model中的数据会被放在请求域中
        model.addAttribute("msg","Hello World!");
        model.addAttribute("link","https://www.baidu.com");
        return "login";
    }


    @PostMapping("/demo")
    public String demo(@RequestParam("headerImg") MultipartFile headerImg,
                       @RequestPart("photos") MultipartFile[] photos) throws IOException {
        log.info("上传的信息:headerImg={},photos={}",headerImg.getSize(),photos.length);
        if(!headerImg.isEmpty()){
            //保存到文件服务器
            //获取文件名
            String originalFilename = headerImg.getOriginalFilename();
            //保存到
            String filepath =  "D:/Shop/test/";
            String fp = filepath + originalFilename;
            File file = new File(fp);
            log.info("路径名称:FP={}",fp);
            headerImg.transferTo(file);
        }

        if(photos.length > 0){
            for(MultipartFile photo : photos){
                if(!photo.isEmpty()){
                    String originalFilename = photo.getOriginalFilename();
                    photo.transferTo(new File("D:\\Shop\\test\\"+originalFilename));
                }
            }
        }
        return "login";
    }
}
