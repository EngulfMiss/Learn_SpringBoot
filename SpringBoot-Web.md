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
1. 导入约束  
```html
<html lang="en" xmlns:th="http://www.thymeleaf.org">
```
2. thymeleaf语法

