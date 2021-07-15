package com.engulf.service;

import com.engulf.service.TicketService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
public class TestController {
    @Reference(version = "1.0",check = true)
    TicketService ticketService;

    @RequestMapping("/kindred")
    public String ticket(){
        return ticketService.getTicket();
    }
}
