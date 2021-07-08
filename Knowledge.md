# 微服务
**什么是微服务？**
微服务是一种架构风格，它要求我们在开发一个应用的时候，这个应用必须构建成一系列小服务的组合；可以通过http，rpc的方式进行互通。  
所谓微服务架构，就是打破之前all in one的架构方式，把每个功能元素独立出来，把独立出来的功能元素动态组合。  
- 好处：
  - 节省了调用资源
  - 每个功能元素的微服务都是一个可替换的，可独立升级的软件代码


## 第一个Springboot程序
IDEA -> Spring Initializr  构建一个springboot项目

**注意点**  
- 我们的包要创建在入口程序的同级目录下  

____

自定义启动banner文字网址[https://www.bootschool.net/ascii](https://www.bootschool.net/ascii)
____

## 原理初探
自动配置：   
pom.xml
- spring-boot-dependencies：核心依赖在父工程中
- 我们在引入springboot依赖时，不需要指定版本，因为springboot有默认配置

启动器  
- 说白了就是Springboot的启动场景
- 比如spring-boot-starter-web，他就会帮我们自动导入web环境所有的依赖
- springboot会将所有的功能场景变成启动器
- 我们要使用什么功能

## 主程序
```java
//程序的主入口
//@SpringBootApplication：标注这个类是一个springboot的应用
@SpringBootApplication
public class HelloworldApplication {

    public static void main(String[] args) {
        //将springboot应用启动
        SpringApplication.run(HelloworldApplication.class, args);
    }

}
```

**注解**
- @SpringBootApplication
  - @SpringBootConfiguration：springboot的配置
    - @Configuration

  - @EnableAutoConfiguration：自动配置
    - @AutoConfigurationPackage：自动配置包
      - @Import({Registrar.class})：自动配置‘包注册’
    - @Import({AutoConfigurationImportSelector.class})：自动配置导入选择  

  - @ComponentScan：


获取所有的配置
```java
List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
```

获取候选的配置
```java
protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
				getBeanClassLoader());
		Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
				+ "are using a custom packaging, make sure that file is correct.");
		return configurations;
	}
```

自动配置的核心文件：MATA-INF/spring.factories  
spring-boot-autoconfigure-2.5.2.jar/META-INF/spring.factories  

**结论**  
springboot所有自动配置都是在启动的时候扫描并加载：  
\META-INF\spring.factories 所有的自动配置类都在这里面，但是不一定生效。要判断条件是否成立，只有导入了对应的starter，对应的配置才会生效


1. springboot在启动的时候，从类路径下/META-INF/spring.factories 获取指定的配置类;
2. 将这些自动配置类导入容器，自动配置类就会生效，帮我们进行自动配置
3. 以前我们需要自动配置的东西，springboot帮我们做了
4. 整合javaEE，解决方案和自动配置的东西都在pring-boot-autoconfigure-2.5.2.jar下
5. 他会把所有需要导入的组件，以类名的方式返回，这些组件就会添加到容器
6. 容器中也会存在很多xxxAutoConfiguration的文件，就是这些配置类给容器中导入了这个场景需要的所有组件

____
## SpringApplication 类
这个类主要做了四件事情  
1. 推断应用的类型是普通项目还是web项目
2. 查找并加载所有可用初始化器，设置到initializers属性中
3. 找出所有的应用程序监听器，设置到listeners属性中
4. 推断并设置main方法的定义类，找到运行的主类


# springboot配置
**springboot使用全局的配置文件，配置文件的名称是固定的**
- application.properties
  - 语法格式：key=value
```properties
server.port=8082
```

- application.yaml/yml
  - 语法格式：key:空格value
```yaml
server:
# 普通的key，value
  port: 8081
# 对象
champion:
  name: kindred
  age: 1500

# 对象(行内写法)
student: {name: kindred,age: 1500}

# 数组
pets:
  - cat
  - dog
  - bird

# 数组(行内写法)
animals: [cat,dog,pig]
```
