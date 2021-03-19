package com.engulf.boot.controller;

import com.engulf.boot.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
public class HelloController {

    @Autowired
    private Person person;

    @RequestMapping("/person")
    public Person showPerson(){
        String userName = person.getUserName();
        System.out.println(userName);
        System.out.println("gnardada\nkindred");
        return person;
    }
}
