package com.engulf.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {
    //我们想在一个特定的时间执行这段代码
    // 秒 分 时 日 月 周几
    /*
        30 15 10 * * ?   每天10点15分30秒
     */
    @Scheduled(cron = "0 * * * * ?")
    public void hello(){
        System.out.println("Hello,你被执行了~");
    }
}
