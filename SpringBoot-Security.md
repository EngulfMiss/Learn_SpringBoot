# SpringSecurity(安全)
## Spring Security简介
Spring Security是针对Spring项目的安全框架，也是Spring Boot底层安全模块默认的技术选型，他可以实现强大的Web安全控制，对于安全控制，我们仅需要引入spring-boot-starter-security模块，进行少量的配置，即可实现强大的安全管理!  
**记住几个类**
- WebSecurityConfigurerAdapter : 自定义Security策略
- AuthenticationManagerBuilder : 自定义认证策略
- @EnableWebSecurity : 开启WebSecurity模式

Spring Security的两个主要目标是“认证”和“授权”(访问控制)

## 使用Spring Security
- 导入Spring Security依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

- 编写一个配置类进行访问权限控制
  - 标注@EnableWebSecurity
  - 继承WebSecurityConfigurerAdapter
```java
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
        http.formLogin();  //开启登录的页面
        
        http.logout().logoutSuccessUrl("/"); //开启注销功能,注销后跳转首页
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
```

**我们想达到一种效果，对应用户有访问哪些功能的权限就显示哪些模块，而不是全部显示出来**  
**可以使用thymeleaf对security的整合实现**  
- 导入依赖  
```xml
<!-- thymeleaf整合security -->
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity5</artifactId>
</dependency>
```

- 添加命名空间
```html
<html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:sec=http://www.thymeleaf.org/extras/spring-security>
```

- 前端代码
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:sec=http://www.thymeleaf.org/extras/spring-security>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>首页</title>
    <!--semantic-ui-->
    <link href="https://cdn.bootcss.com/semantic-ui/2.4.1/semantic.min.css" rel="stylesheet">
    <link th:href="@{/qinjiang/css/qinstyle.css}" rel="stylesheet">
</head>
<body>

<!--主容器-->
<div class="ui container">

    <div class="ui segment" id="index-header-nav" th:fragment="nav-menu">
        <div class="ui secondary menu">
            <a class="item"  th:href="@{/index}">首页</a>

            <!--登录注销-->
            <div class="right menu">
                <!--如果未登录-->
                <div sec:authorize="!isAuthenticated()">
                    <a class="item" th:href="@{/toLogin}">
                        <i class="address card icon"></i> 登录
                    </a>
                </div>

                <!--如果登录：用户名 和 注销-->
                <!-- 用户名 -->
                <div sec:authorize="isAuthenticated()">
                        用户名:<span sec:authentication="name"></span>  <!--登录security成功后可以用这个获取用户名-->
                        权限:<span sec:authentication="principal.authorities"></span>  <!--登录security成功后可以用这个获取用户权限-->
                </div>
                <!--注销-->
                <div sec:authorize="isAuthenticated()">
                    <a class="item" th:href="@{/logout}">
                        <i class="sign-out icon"></i> 注销
                    </a>
                </div>
                <!--已登录
                <a th:href="@{/usr/toUserCenter}">
                    <i class="address card icon"></i> admin
                </a>
                -->
            </div>
        </div>
    </div>

    <div class="ui segment" style="text-align: center">
        <h3>Spring Security Study</h3>
    </div>

    <div>
        <br>
        <div class="ui three column stackable grid">
            <!-- 显示的菜单根据用户的权限动态显示 -->
            <div class="column" sec:authorize="hasRole('vip1')">
                <div class="ui raised segment">
                    <div class="ui">
                        <div class="content">
                            <h5 class="content">Level 1</h5>
                            <hr>
                            <div><a th:href="@{/level1/1}"><i class="bullhorn icon"></i> Level-1-1</a></div>
                            <div><a th:href="@{/level1/2}"><i class="bullhorn icon"></i> Level-1-2</a></div>
                            <div><a th:href="@{/level1/3}"><i class="bullhorn icon"></i> Level-1-3</a></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="column" sec:authorize="hasRole('vip2')">
                <div class="ui raised segment">
                    <div class="ui">
                        <div class="content">
                            <h5 class="content">Level 2</h5>
                            <hr>
                            <div><a th:href="@{/level2/1}"><i class="bullhorn icon"></i> Level-2-1</a></div>
                            <div><a th:href="@{/level2/2}"><i class="bullhorn icon"></i> Level-2-2</a></div>
                            <div><a th:href="@{/level2/3}"><i class="bullhorn icon"></i> Level-2-3</a></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="column" sec:authorize="hasRole('vip3')">
                <div class="ui raised segment">
                    <div class="ui">
                        <div class="content">
                            <h5 class="content">Level 3</h5>
                            <hr>
                            <div><a th:href="@{/level3/1}"><i class="bullhorn icon"></i> Level-3-1</a></div>
                            <div><a th:href="@{/level3/2}"><i class="bullhorn icon"></i> Level-3-2</a></div>
                            <div><a th:href="@{/level3/3}"><i class="bullhorn icon"></i> Level-3-3</a></div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>

</div>


<script th:src="@{/qinjiang/js/jquery-3.3.1.min.js}"></script>
<script th:src="@{/qinjiang/js/semantic.min.js}"></script>

</body>
</html>
```
