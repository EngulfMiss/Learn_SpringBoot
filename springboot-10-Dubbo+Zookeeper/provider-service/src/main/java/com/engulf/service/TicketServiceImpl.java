package com.engulf.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

//zookeeper：服务注册和发现

@Service(version = "1.0")  //注册
@Component  //使用了Dubbo后尽量不要使用@Service注解
public class TicketServiceImpl implements TicketService {
    @Override
    public String getTicket() {
        return "绽灵节门票";
    }
}
