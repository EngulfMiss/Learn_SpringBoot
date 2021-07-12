package com.engulf.config;

//全面扩展SpringMVC

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Locale;

//如果想自定义一些功能只需要写这个组件然后将这个组件交给springboot管理

@Configuration
//@EnableWebMvc  //这个注解就是导入了一个类：DelegatingWebMvcConfiguration，这个类可以从容器中获取所有WebMvcConfigurer
public class MyMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginHandlerInterceptor loginHandlerInterceptor;
    //自定义的国际化解析器
//    @Bean
//    //SpringMVC中约定国际化解析器的Bean名称必须是localeResolver
//    public LocaleResolver localeResolver(){
//        return new MyLocaleResolver();
//    }


    //添加自定义拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginHandlerInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/login","/","/index","/index.html","css/**","js/**","/images/**","/fonts/**");
    }
}
