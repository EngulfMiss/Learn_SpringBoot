package com.engulf.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //3.ShiroFilterFactoryBean
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(securityManager);

        //添加shiro的内置过滤器
        /*
            anon：无需认证就可以访问
            authc：必须认证了才能访问
            user：必须拥有 记住我 功能才能用
            perms：拥有对某个资源的权限才能访问
            role：拥有某个角色权限才能访问
         */
        // public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap)
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 授权操作(访问什么请求需要什么权限)  授权拦截操作要在登录拦截之前
        filterChainDefinitionMap.put("/champion/add","perms[user:add]");
        filterChainDefinitionMap.put("/champion/update","perms[user:update]");



        //登录拦截
        // filterChainDefinitionMap.put("/champion/add","anon");
        //设置什么请求，怎么访问  例如,请求/champion/add 无需认证就可以访问
//        filterChainDefinitionMap.put("/champion/add","authc");
//        filterChainDefinitionMap.put("/champion/update","authc");
        filterChainDefinitionMap.put("/champion/*","authc");


        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        //设置登录请求
        bean.setLoginUrl("/toLogin");

        //设置未授权请求
        bean.setUnauthorizedUrl("/Unauthorized");

        return bean;
    }


    //2.DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("championRealm") ChampionRealm championRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联管理realm
        securityManager.setRealm(championRealm);
        return securityManager;
    }


    //1.创建realm对象，需要自定义
    @Bean
    public ChampionRealm championRealm(){
        return new ChampionRealm();
    }

    //整合ShiroDialect：用来整合 shiro thymeleaf
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
