package com.engulf.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //首页所有人都可以访问，但是功能页只有对应权限的人可以访问
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");

        //没有权限默认跳到登录页面
        //loginPage登录用的页面
        //loginProcessingUrl登录认证用的页面
        //开启登录的页面
        http.formLogin().loginPage("/toLogin").loginProcessingUrl("/login")
        .usernameParameter("user").passwordParameter("psw");

        http.logout().logoutSuccessUrl("/"); //开启注销功能,注销后跳转首页

//        http.csrf().disable();  //关闭csrf功能，注销失败的原因


        //开启记住我功能
        http.rememberMe().rememberMeParameter("remember");
    }


    //认证
    //密码编码：There is no PasswordEncoder mapped for the id "null" (密码需要加密)
    //在Spring Secutiry 5.0+ 新增了很多的加密方法
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //在内存中认证
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
        .withUser("kindred").password(new BCryptPasswordEncoder().encode("w2snowgnar")).roles("vip2","vip3")
        .and().withUser("root").password(new BCryptPasswordEncoder().encode("w2snowgnar")).roles("vip1","vip2","vip3")
        .and().withUser("gnar").password(new BCryptPasswordEncoder().encode("w2snowgnar")).roles("vip1","vip2");
    }
}
