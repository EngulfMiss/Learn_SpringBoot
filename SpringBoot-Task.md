# 任务
## 异步任务
- 在你的异步方法上添加@Async注解
```java
@Service
public class AsyncService {
    //告诉Spring这是一个异步的方法
    @Async
    public void hello(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据正在处理......");
    }
}
```

- 在启动类上开启异步支持
```java
@SpringBootApplication
@EnableAsync   //开启异步功能
public class Springboot09TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot09TaskApplication.class, args);
    }
}
```
