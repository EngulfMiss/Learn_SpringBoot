package com.engulf;

import com.engulf.pojo.User;
import com.engulf.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShiroSpringbootApplicationTests {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void contextLoads() {
        User gnar = userService.getUserByName("Gnar");
        System.out.println(gnar);
    }

}
