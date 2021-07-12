# Learn_SpringBoot
学习springboot

## springboot的配置文件名称
application.properties

## 新建SpringBoot项目运行页面报错Whitelabel Error Page This application has no explicit mapping for /error, so yo
这个错误的出现一般是SpringBoot的启动类（类名上面有@SpringBootApplication注解 ）与controller包不在同一个目录下，解决方案就是把启动类和controller包放在同目录下就可以啦.
控制器文件夹要和启动类同级

## SpringBoot引用thymeleaf 
要运行和代码提示，在html页面上加上
```
<html lang="en" xmlns:th="http://www.thymeleaf.org">
```
