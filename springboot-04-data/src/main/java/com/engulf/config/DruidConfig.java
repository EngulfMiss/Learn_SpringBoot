package com.engulf.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    //后台监控
    //因为Springboot内置了servlet容器，所以没有web.xml，替代方法 ServletRegisterBean
    @Bean
    public ServletRegistrationBean registrationBean(){
        //注册后台监控和进入的路径
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        //账号密码配置
        HashMap<String,String> map = new HashMap<>();

        //增加登录配置
        map.put("loginUsername","admin");   //两个key是固定的
        map.put("loginPassword","123456");

        //允许谁可以访问
        map.put("allow","");

        //禁止谁访问
//        map.put("deny","192.231.45.46");

        bean.setInitParameters(map);  //设置初始化参数
        return bean;
    }

    // filter
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //过滤哪些请求
//        filterRegistrationBean.addUrlPatterns("/*");
        //排除哪些请求
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
