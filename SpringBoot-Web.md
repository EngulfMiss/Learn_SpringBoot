# SpringBoot Web开发
**要解决的问题**
- 导入静态资源
- 首页
- jsp，模板引擎Thymeleaf
- 装配扩展SpringMVC
- CRUD
- 拦截器
- 国际化

____
## 导入静态资源
源码
```java
@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			if (!this.resourceProperties.isAddMappings()) {
				logger.debug("Default resource handling disabled");
				return;
			}
      //addResourceHandler方法的第二个参数是发送请求的路径  第三个参数是请求映射到的资源位置
			addResourceHandler(registry, "/webjars/**", "classpath:/META-INF/resources/webjars/");
			addResourceHandler(registry, this.mvcProperties.getStaticPathPattern(), (registration) -> {
				registration.addResourceLocations(this.resourceProperties.getStaticLocations());
				if (this.servletContext != null) {
					ServletContextResource resource = new ServletContextResource(this.servletContext, SERVLET_LOCATION);
					registration.addResourceLocations(resource);
				}
			});
		}
```


分三种情况
- 用户自定义静态资源访问方式
```properties
spring.mvc.static-path-pattern=/xxx   # 有了自定义的路径 默认的就会失效
```

- webjars默认访问方式
- springboot默认访问方式


springboot默认访问方式(静态资源目录)
```java
this.resourceProperties.getStaticLocations()

public String[] getStaticLocations() {
    return this.staticLocations;
  }
  
  private String[] staticLocations = CLASSPATH_RESOURCE_LOCATIONS;

  private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
				"classpath:/resources/", "classpath:/static/", "classpath:/public/" };

```

**总结**  
1. 在springboot，我们可以使用以下方式处理静态资源
  - webjars    localhost:8080/webjars/
  - /**,resource,static,public      localhost:8080/  

2.优先级
resource > static(默认) > public
_____


_____

### 首页和图标定制  
首页:默认会去静态资源目录找index.html  
```java
private Resource getWelcomePage() {
			for (String location : this.resourceProperties.getStaticLocations()) {
				Resource indexHtml = getIndexHtml(location);
				if (indexHtml != null) {
					return indexHtml;
				}
			}
			ServletContext servletContext = getServletContext();
			if (servletContext != null) {
				return getIndexHtml(new ServletContextResource(servletContext, SERVLET_LOCATION));
			}
			return null;
		}

		private Resource getIndexHtml(String location) {
			return getIndexHtml(this.resourceLoader.getResource(location));
		}

		private Resource getIndexHtml(Resource location) {
			try {
				Resource resource = location.createRelative("index.html");
				if (resource.exists() && (resource.getURL() != null)) {
					return resource;
				}
			}
			catch (Exception ex) {
			}
			return null;
		}
```

图标：  
在2.x以前的版本。直接将你需要的favicon.ico文件倒挡static下面就可以。

2.X以后的版本，消息了自动配置，需要人手动去在每一个页面添加；  
在static目录的images目录（也可以放在static下的其他目录）下添加名字为favicon.ico的图标，然后在每个页面添加如下代码，引入即可。  
```html
<link rel="icon" type="image/x-icon" href="/images/favicon.ico">
```
_____
## 模板引擎
引入依赖
```xml
<!-- Thymeleaf -->
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf-spring5</artifactId>
</dependency>
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-java8time</artifactId>
</dependency>
```
或者直接导入thymeleaf启动器
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

使用thymeleaf  
**要想springboot识别thymeleaf模板，就要加入下面的约束才可以**  
1. 导入约束  
```html
<html lang="en" xmlns:th="http://www.thymeleaf.org">
```
2. thymeleaf语法
基础文本：  
```java
model.addAttribute("msg","<h1>Kindred</h1>");
```
```html
<!--th:text-->
<div th:text="${msg}">啦啦啊</div>
<div th:utext="${msg}">啦啦啊</div>
```
- th:text:不转义字符串输出结果为：&lt;h1&gt;Kindred&lt;/h1&gt;
- th:utext:转义字符串输出结果为：Kindred带h1标签效果  

普通集合
```java
model.addAttribute("champions", Arrays.asList("gnar","kindred"));
```
```html
<!--th:each-->
<h3 th:each="champion:${champions}" th:text="${champion}"></h3>
<h3 th:each="champion:${champions}">[[${champion}]]</h3>
```

## 扩展自定义springMVC  
如果我们要扩展springmvc，官方建议我们这样做  
写一个类添加@Configuration注解并且实现WebMvcConfigurer接口
```java
//如果想自定义一些功能只需要写这个组件然后将这个组件交给springboot管理

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    //视图跳转
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/engulf").setViewName("test");
    }

    // public interface ViewResolver 实现了视图解析器接口的类，我们就可以把他看做视图解析器

    //将自定义的视图解析器放入spring容器中
    @Bean
    public ViewResolver myViewResolver(){
        return new MyViewResolver();
    }

    //自定义一个自己的视图解析器
    public static class MyViewResolver implements ViewResolver {
        @Override
        public View resolveViewName(String s, Locale locale) throws Exception {
            return null;
        }
    }
}
```

## web项目
1. 首页实现  
如果首页想使用thymeleaf模板，一定要注意给自己的html页面添加  
```html
<html lang="en" xmlns:th="http://www.thymeleaf.org">
```  
并且推荐里面的资源路径使用thymeleaf推荐的@{}形式
```html
<script th:src="@{js/jquery-1.10.2.min.js}"></script>
<script th:src="@{js/bootstrap.min.js}"></script>
<script th:src="@{js/modernizr.min.js}"></script>
```

**国际化**
写三个(一个基础，两个语言)配置文件  
- login.properties
```properties
login.password=密码
login.register=去注册
login.tip=请登录
login.username=用户名
```
- login_en_US.properties
```properties
login.password=password
login.register=Sign Up
login.tip=Please Sign In
login.username=username
```
- login_zh_CN.properties
```properties
login.password=密码
login.register=去注册
login.tip=请登录
login.username=用户名
```

IDEA自动帮我们创建了一个Resource Bundle 'login'  

然后在springboot配置文件中声明国际化配置文件的位置
```properties
# 我们的国际化配置文件的真实位置
spring.messages.basename=i18n.login
```

在thymeleaf页面中使用#{}的方式获取国际化内容
```html
<input type="text" class="form-control" th:placeholder="#{login.username}" autofocus>
<input type="password" class="form-control" th:placeholder="#{login.password}">
```

并在页面中添加两个语言切换的链接
```html
<!-- 跳转的页面要有对应的controller处理
	l='zh_CN'，l 是参数 可以写其他的
-->
<a class="btn btn-sm" th:href="@{/login(l='zh_CN')}">中文</a>
<a class="btn btn-sm" th:href="@{/login(l='en_US')}">English</a>
```

编写一个自定义的国际化解析器(实现LocaleResolver接口)  
**注意**  
- SpringMVC中约定国际化解析器的Bean名称必须是localeResolver
- SpringMVC中约定国际化解析器的Bean名称必须是localeResolver
- SpringMVC中约定国际化解析器的Bean名称必须是localeResolver
- 我们自定义的国际化组件一定要放到spring容器中
```java
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
```

## 拦截器
- 自己写一个类实现HandlerInterceptor接口，重写方法，一般就重写preHandle
```java
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
```

- 注册自己创建的拦截器，实现WebConfigurer接口
```java
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginHandlerInterceptor loginHandlerInterceptor;


    //添加自定义拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginHandlerInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/login","/","/index","/index.html","css/**","js/**","/images/**","/fonts/**");
    }
}
```

## Thymeleaf公共页面提取
**两种声明公共页的方式 使用~{}**
- 使用id
```html
<div id="leftmenu" class="left-side sticky-left-side"> //提取公共页面
//使用
<div th:replace="common :: #leftmenu"></div>  //id公共页使用加#
```
- 使用thymeleaf提供的th:fragment提取  
```html
<div th:fragment="headermenu" class="header-section"> 
//使用
<div th:replace="~{common :: headermenu}"></div>
```

实现点击标签高亮的一个思路  
在跳转页面的时候传递一个参数用来判断当前页面  
使用三元运算符判断高亮是否开启  


## 表单数据遍历案例(伪数据库实现)
- 创建实体类Employee
```java
@Data
public class Employee {
    private Integer id;
    private String Ename;
    private String email;
    private Integer gender;

    private Department department;
    private Date birth;
}
```
- 控制器处理请求，返回数据给前端
```java
@Controller
public class EmployeeController {
    @Autowired
    private EmpolyeeDao empolyeeDao;

    //查询所有员工信息
    @GetMapping("/selectAllEmploy")
    public String selectAllEmploy(Model model){
        model.addAttribute("employees",empolyeeDao.getAllEmployees());
        return "dynamic_table";
    }
}
```
- 前端遍历数据
```html
<tbody>
	<tr class="gradeX" th:each="employee:${employees}">
	    <td th:text="${employee.getId()}"></td>
	    <td>[[${employee.getEname()}]]</td>
	    <td th:text="${employee.getEmail()}"></td>
	    <td th:text="${employee.getGender()==0?'女':'男'}"></td>
	    <td th:text="${employee.getDepartment().getDepartmentName()}"></td>
	    <td th:text="${#dates.format(employee.getBirth(),'yyyy/MM/dd HH:mm')}"></td>
	</tr>
</tbody>
```
