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
    public Docket docket(Environment environment){

        //获取项目的环境
        Profiles profiles = Profiles.of("dev");
        //通过environment.acceptsProfiles判断是否处于设定的环境中
        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .enable(flag)  //是否启用swagger，false就不能使用swagger了
                .select()
                //RequestHandlerSelectors:配置要扫描的方式
                //basePackage:指定要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.engulf.swagger.controller"))
                //paths():过滤路径
                //ant:什么样的请求才可以被扫描到(请求为/engulf/所有才可以被扫描到，控制器处理的请求中没有/engulf开头的请求，所以什么都扫不到)
                //.paths(PathSelectors.ant("/engulf/**"))
                .build();
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
## Swagger配置扫描接口
- Docket.select().apis(RequestHandlerSelectors.basePackage("com.engulf.swagger.controller")).build();
  - apis方法参数RequestHandlerSelectors:配置要扫描的方式
    - basePackage:指定要扫描的包
    - any:扫描全部
    - none:都不扫描
    - withClassAnnotation:扫描类上的注解，参数是一个注解的反射对象 Xxx.class
    - withMethodAnnotation:扫描方法上的注解

  - paths方法参数PathSelectors:配置要扫描的路径
    - any:任何
    - none:任何不
    - regex:正则表达式
    - ant:路径 例如：PathSelectors.ant("/engulf/**")

```java
return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .enable(flag)  //是否启用swagger，false就不能使用swagger了
                .select()
                //RequestHandlerSelectors:配置要扫描的方式
                //basePackage:指定要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.engulf.swagger.controller"))
                //paths():过滤路径
                //ant:什么样的请求才可以被扫描到(请求为/engulf/所有才可以被扫描到，控制器处理的请求中没有/engulf开头的请求，所以什么都扫不到)
                .paths(PathSelectors.ant("/engulf/**"))
                .build();
```

## 配置API文档的分组
```java
.groupName("Engulf迷失")
```

**如何多个分组**  
- 可以写多个Docket
```java
@Bean
public Docket docket3(){
    return new Docket(DocumentationType.SWAGGER_2).groupName("Gnar");
}

@Bean
public Docket docket2(){
    return new Docket(DocumentationType.SWAGGER_2).groupName("Kindred");
}
```

## 扫描实体类
- 编写一个pojo类(属性想要被扫描显示，属性要public修饰，private修饰要添加getter)
```java
public class Champion {
    public String username;
    public String password;
}
```
- 只要我们的接口的返回值中存在实体类，就会被swagger扫描到
```java
//只要我们的接口的返回值中存在实体类，就会被swagger扫描到
@GetMapping("/champion")
public Champion champion(){
    return new Champion();
}
```

- 为实体类添加注释
  - @Api(tags = ""): 给控制器类添加注释
  - @ApiModel(""): 给pojo类添加注释
  - @ApiModelProperty(""): 给字段添加注释
  - @ApiOperation(""): 给控制器方法的注释

用法示例如下：  

```java
@ApiModel("用户实体类")
public class Champion {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;

    ... ...
}


@Api(tags = "名为Hello的控制器类")
@RestController
public class HelloController {
    @GetMapping(value = "/hello")
    public String hello(){
        return "Hello World";
    }

    //只要我们的接口的返回值中存在实体类，就会被swagger扫描到
    @GetMapping("/champion")
    public Champion champion(){
        return new Champion();
    }

    @GetMapping("/opt/{name}")
    @ApiOperation("名为operation的方法")  //给控制器方法的注释
    public String operation(@PathVariable("name") @ApiParam("用户名") String username){
        return "Hello" + username;
    }
}
```
