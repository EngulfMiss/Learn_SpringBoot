## 分布式理论
- 什么是分布式系统?
在《分布式系统原理与范型》一书中有如下定义:“分布式系统是若干独立计算机的集合，这些计算机对于用户来说就像单个相关系统”;  
分布式系统是由一组通过网络进行通信、为了完成共同的任务而协调工作的计算机节点组成的系统。分布式系统的出现是为了用廉价的、  
普通的机器完成单个计算机无法完成的计算、存储任务。其目的是利用更多的机器，处理更多的数据。

## RPC
- 什么是RPC?
RPC 【Remote Procedure Call】是指远程过程调用，是一种进程间通信方式，他是一种技术的思想,而不是规范。它允许  
程序调用另一个地址空间(通常是共享网络的另一台机器上)的过程或函数，而不用程序员显式编码这个远程调用的细节。即  
程序员无论是调用本地的还是远程的函数，本质上编写的调用代码基本相同。

- RPC的两个核心：通讯和序列化

# Dubbo
## Dubbo概念
- 什么是Dubbo?  
Apache Dubbo 是一款高性能、轻量级的开源Java RPC框架，它提供了三大核心能力:面向接口的远程方法调用，智能容错和负载均衡，以及服务自动注册和发现。  
![Dubbo](https://camo.githubusercontent.com/e11a2ff9575abc290657ba3fdbff5d36f1594e7add67a72e0eda32e449508eef/68747470733a2f2f647562626f2e6170616368652e6f72672f696d67732f6172636869746563747572652e706e67)

- 服务提供者(Provider):暴露服务的服务提供方，服务提供方在启动时，向注册中心注册自己提供的服务
- 服务消费者(Consumer):调用远程服务的服务消费方，服务消费者在启动时，向注册中心订阅自己所需要的服务，服务消费者，从提供者地址列表中，基于软负载均衡  
算法，选一台提供者进行调用，如果调用失败，再选另一台调用
- 注册中心(Registry):注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者
- 监控中心(Monitor):服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心

_____
**zookeeper注册中心**  
出现的问题，启动apache-zookeeper-3.7.0\bin\zkServer.cmd闪退  
复制apache-zookeeper-3.7.0\conf\zoo_sample.cfg  重命名为 zoo.cfg   
下载地址：[https://zookeeper.apache.org/releases.html#verifying](https://zookeeper.apache.org/releases.html#verifying)
_____

_____
**dubbo-admin监控管理后台下载**  
下载地址：[https://github.com/apache/dubbo-admin/tree/master](https://github.com/apache/dubbo-admin/tree/master)

在dubbo-admin-master/ 目录内使用 cmd   
输入 mvn clean package -Dmaven.test.skip=true ,将其打成jar包  
在 dubbo-admin-master\dubbo-admin\target 运行jar包  
控制台输入 java -jar dubbo-admin-0.0.1-SNAPSHOT.jar，记得要打开zookeeper  
进入网址 http://localhost:7001 用户名，密码都为root

_____

## 服务注册发现实战
- 新建项目，创建两个Model
- 导入依赖(提供者，消费者都要)
```xml
        <!--导入依赖 Dubbo+zookeeper-->
        <!--Dubbo-->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>2.7.10</version>
        </dependency>

        <!--zookeeper-->
        <dependency>
            <groupId>com.github.sgroschupf</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.1</version>
            <exclusions>
                <exclusion>
                    <artifactId>zookeeper</artifactId>
                    <groupId>org.apache.zookeeper</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <!--日志会冲突-->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>5.1.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-x-discovery</artifactId>
            <version>5.1.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>5.1.0</version>
            <exclusions>
                <exclusion>
                    <artifactId>zookeeper</artifactId>
                    <groupId>org.apache.zookeeper</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.7.0</version>
            <!-- 排除这个slf4j-log4j12 -->
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
                <exclusion>
                    <artifactId>log4j</artifactId>
                    <groupId>log4j</groupId>
                </exclusion>
            </exclusions>
        </dependency>
```
**提供者**  
- 编写服务并提交给注册中心完成注册
```java
import com.engulf.service.TicketService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

//zookeeper：服务注册和发现

@DubboService  //注册
@Component  //使用了Dubbo后尽量不要使用@Service注解
public class TicketServiceImpl implements TicketService {
    @Override
    public String getTicket() {
        return "绽灵节门票";
    }
}
```
- 编写配置文件
```properties
server.port=8081
# 服务应用名字
dubbo.application.name=provider-service
# 注册中心地址
dubbo.registry.address=zookeeper://127.0.0.1:2181
# 哪些服务要被注册
dubbo.scan.base-packages=com.engulf.service

dubbo.registry.protocol=zookeeper

zookeeper.timeout=40000
```

**消费者**
- 消费者使用@DubboReference来接收远程服务
```java
@RestController
@Component
public class TestController {
    @DubboReference(version = "1.0",check = true)
    TicketService ticketService;

    @RequestMapping("/kindred")
    public String ticket(){
        return ticketService.getTicket();
    }
}
```

- 消费者的配置文件
```properties
server.port=8082
# 消费者去哪里拿服务(注册中心)，需要暴露自己的名字
dubbo.application.name=consumer-service
# 注册中心的地址
dubbo.registry.address=zookeeper://127.0.0.1:2181

dubbo.registry.protocol=zookeeper
```
