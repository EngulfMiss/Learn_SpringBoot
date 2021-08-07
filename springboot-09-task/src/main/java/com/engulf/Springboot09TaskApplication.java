package com.engulf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync   //开启异步功能
@EnableScheduling  //开启定时功能的注解
public class Springboot09TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot09TaskApplication.class, args);
    }

}
