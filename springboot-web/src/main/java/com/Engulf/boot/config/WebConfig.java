package com.Engulf.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;
//开启矩阵变量使用
//方案1：
//    配置类实现WebMvcConfigurer接口
//    重写configurePathMatch方法
//    设置不移除；后面内容

/*
方案2：
    添加WebMvcConfigurer组件(@Bean)
    重写configurePathMatch方法
*/

@Configuration(proxyBeanMethods = false)
public class WebConfig /* 方案1： implements WebMvcConfigurer*/ {

    @Bean  //WebMvcConfigurer
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper urlPathHelper = new UrlPathHelper();
                //设置为不移除;后面的内容，矩阵变量就可以生效
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }
        };
    }

/*
    方案1：
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        //设置为不移除;后面的内容，矩阵变量就可以生效
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }
    */
}
