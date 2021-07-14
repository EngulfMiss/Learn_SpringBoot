# Swagger
## Swagger简介
**前后端分离**  

- 号称世界上最流行的Api框架
- RestFul Api文档在线自动生成 -> Api文档与API定义同步更新
- 直接运行，可以在线测试API接口
- 支持多种语言

## Swagger的使用
- swagger2
- ui

## SpringBoot集成Swagger
- 导入相关依赖(3.0版进不来swagger-ui)
```xml
 <dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>

<!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger-ui -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
```

- 编写一个helloworld工程
- 编写swagger配置类
```java
@Configuration
@EnableSwagger2  //开启Swagger2
public class SwaggerConfig {
}
```

## 配置Swagger
- Swagger的bean实例Docket
- 自定义ApiInfo(两种方式)
    - 第一种直接通过构造器new
    - 第二种使用ApiInfoBuilder
```java
@Configuration
@EnableSwagger2  //开启Swagger2
public class SwaggerConfig {
    //配置了Swagger Docket的bean实例
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo());
    }

    //配置Swagger信息
    private ApiInfo getApiInfo(){

        //作者信息
        Contact contact = new Contact("EngulfM","http://www.gnardada.com","1216982545@qq.com");

        ApiInfo build = new ApiInfoBuilder().version("2.9.2").description("情人眼里出西施").build();

        /*return new ApiInfo("EngulfM的API文档",
                "色不迷人人自迷",
                "v-2.9.2",
                "http://www.gnardada.com",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                 new ArrayList());*/
        return build;
    }
}
```
