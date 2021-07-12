package com.engulf.config;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Component("localeResolver")
public class MyLocaleResolver implements LocaleResolver {

    //解析请求
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        //获取请求中的参数
        String language = httpServletRequest.getParameter("l");
        Locale locale = Locale.getDefault();  //如果没有使用默认的

        //判断请求是否携带了国际化参数
        if(!StringUtils.isEmpty(language)){
            //zh_CN
            String[] s = language.split("_");
            //通过_分隔为国家和地区
            locale = new Locale(s[0], s[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
