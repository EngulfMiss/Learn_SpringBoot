# Learn_SpringBoot
学习springboot

## springboot web项目的pox.xml配置
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.4.RELEASE</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>

<!-- 可执行jar包插件 -->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

## springboot的配置文件名称
application.properties

## 新建SpringBoot项目运行页面报错Whitelabel Error Page This application has no explicit mapping for /error, so yo
这个错误的出现一般是SpringBoot的启动类（类名上面有@SpringBootApplication注解 ）与controller包不在同一个目录下，解决方案就是把启动类和controller包放在同目录下就可以啦.
控制器文件夹要和启动类同级
