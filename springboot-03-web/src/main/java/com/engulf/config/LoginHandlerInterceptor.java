package com.engulf.config;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登录成功后，应该有用户的session
        if(request.getSession().getAttribute("LoginUser") != null){
            return true;
        }else {
            request.setAttribute("errorMsg","请先登录");
            request.getRequestDispatcher("/login").forward(request,response);
            return false;
        }
    }
}
