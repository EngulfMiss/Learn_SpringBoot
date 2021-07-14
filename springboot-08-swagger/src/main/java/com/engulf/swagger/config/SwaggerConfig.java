package com.engulf.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2  //开启Swagger2
public class SwaggerConfig {
    @Bean
    public Docket docket3(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("Gnar");
    }

    @Bean
    public Docket docket2(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("Kindred");
    }


    //配置了Swagger Docket的bean实例
    @Bean
    public Docket docket(Environment environment){

        //获取项目的环境
        Profiles profiles = Profiles.of("dev");
        //通过environment.acceptsProfiles判断是否处于设定的环境中
        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .enable(true)  //是否启用swagger，false就不能使用swagger了
                .groupName("Engulf迷失")
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
