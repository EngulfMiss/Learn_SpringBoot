# 微服务
**什么是微服务？**
微服务是一种架构风格，它要求我们在开发一个应用的时候，这个应用必须构建成一系列小服务的组合；可以通过http，rpc的方式进行互通。  
所谓微服务架构，就是打破之前all in one的架构方式，把每个功能元素独立出来，把独立出来的功能元素动态组合。  
- 好处：
  - 节省了调用资源
  - 每个功能元素的微服务都是一个可替换的，可独立升级的软件代码


## 第一个Springboot程序
IDEA -> Spring Initializr  构建一个springboot项目

**注意点**  
- 我们的包要创建在入口程序的同级目录下  

____

自定义启动banner文字网址[https://www.bootschool.net/ascii](https://www.bootschool.net/ascii)
____

## 原理初探
自动配置：   
pom.xml
- spring-boot-dependencies：核心依赖在父工程中
- 我们在引入springboot依赖时，不需要指定版本，因为springboot有默认配置

启动器  
- 说白了就是Springboot的启动场景
- 比如spring-boot-starter-web，他就会帮我们自动导入web环境所有的依赖
- springboot会将所有的功能场景变成启动器
- 我们要使用什么功能

## 主程序
```java
//程序的主入口
//@SpringBootApplication：标注这个类是一个springboot的应用
@SpringBootApplication
public class HelloworldApplication {

    public static void main(String[] args) {
        //将springboot应用启动
        SpringApplication.run(HelloworldApplication.class, args);
    }

}
```

**注解**
- @SpringBootApplication
  - @SpringBootConfiguration：springboot的配置
    - @Configuration

  - @EnableAutoConfiguration：自动配置
    - @AutoConfigurationPackage：自动配置包
      - @Import({Registrar.class}) 
    - @Import({AutoConfigurationImportSelector.class})
  - @ComponentScan：
