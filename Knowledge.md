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

**自动配置再理解**   
以HttpEncodingAutoConfiguration为例  
```java
//声明这是一个配置类
@Configuration(proxyBeanMethods = false)

//开启配置属性，可以配置的属性来自ServerProperties.class 这里面写了yaml中可以配置的属性
@EnableConfigurationProperties(ServerProperties.class)

//Spring的底层注解，根据条件判断Bean或者类是否生效
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(CharacterEncodingFilter.class)
@ConditionalOnProperty(prefix = "server.servlet.encoding", value = "enabled", matchIfMissing = true)
```

| @Conditional扩展注解  | 作用（判断是否满足当前指定条件)  |
|  ----  | ----  |
| @ConditionalOnJava  | 系统的java版本是否符合要求 |
| @ConditionalOnBean  | 容器中存在指定Bean |
| @ConditionalOnMissingBean  | 容器中不存在指定Bean  |
| @ConditionalOnExpression  | 满足SpEL表达式指定  |
| @ConditionalOnClass  | 系统中有指定的类  |
| @ConditionalOnMissingClass  | 系统中没有指定的类  |
| @ConditionalOnSingleCandidate  | 容器中只有一个指定的Bean，或者这个Bean是首选Bean  |
| @ConditionalOnProperty  | 系统中指定的属性是否有指定的值  |
| @ConditionalOnResource  | 类路径下是否存在指定资源文件  |
| @ConditionalOnWebApplication  | 当前是web环境  |
| @ConditionalOnNotWebApplication  | 当前不是web环境  |
| @ConditionalOnJndi  | JNDI存在指定项  |

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
# 对空格非常严格
# 能将属性注入到配置类中
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

**yaml可以直接给实体类赋值**
假设有实体类Person
```java
@Component
@ConfigurationProperties(prefix = "oneperson")
public class Person {
    private String name;
    private int age;
    private Boolean isHappy;
    private Date birthday;
    private Map<String,Object> maps;
    private List<Object> list;
    private Dog dog;
	......
}
```

通过yaml配置文件为实体类赋值
```yaml
oneperson:
  name: QSJ
  age: 23
  happy: false
  birthday: 1998/12/24
  maps: {k1: v1,k2: v2}
  list:
    - code
    - music
    - game
  dog:
    name: 旺旺
    age: 3
```

**注意事项**
1. 使用@ConfigurationProperties(prefix = "yaml配置文件中的keyName")
2. 使用@ConfigurationProperties报红解决方案，在pom.xml中添加
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```
3. yaml中key的名字对应的是实体类的属性名称  
实体类的属性名是  getter/setter去掉get/set后的单词首字母小写形式  
例如：  
```java
private Boolean isHappy;  
public void setHappy(Boolean happy) {
	isHappy = happy;
}  
//属性名为 happy而不是isHappy
```

### yaml支持松散绑定和JSR303校验

松散绑定  
last-name --> lastName

____

JSR303校验

- 使用注解(@Validated)
- 引入pom.xml依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
- 根据需要使用下列注解进行校验

**空检查**
@Null 验证对象是否为null  
@NotNull 验证对象是否不为null, 无法查检长度为0的字符串  
@NotBlank 检查约束字符串是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格.  
@NotEmpty 检查约束元素是否为NULL或者是EMPTY.  

**Booelan检查**  
@AssertTrue 验证 Boolean 对象是否为 true  
@AssertFalse 验证 Boolean 对象是否为 false  

**长度检查**  
@Size(min=, max=) 验证对象（Array,Collection,Map,String）长度是否在给定的范围之内  
@Length(min=, max=) Validates that the annotated string is between min and max included.  

**日期检查**  
@Past 验证 Date 和 Calendar 对象是否在当前时间之前，验证成立的话被注释的元素一定是一个过去的日期  
@Future 验证 Date 和 Calendar 对象是否在当前时间之后 ，验证成立的话被注释的元素一定是一个将来的日期  
@Pattern 验证 String 对象是否符合正则表达式的规则，被注释的元素符合制定的正则表达式，regexp:正则表达式 flags: 指定 Pattern.Flag 的数组，表示正则表达式的相关选项。  

**数值检查**  
建议使用在Stirng,Integer类型，不建议使用在int类型上，因为表单值为“”时无法转换为int，但可以转换为Stirng为”“,Integer为null  
@Min 验证 Number 和 String 对象是否大等于指定的值  
@Max 验证 Number 和 String 对象是否小等于指定的值  
@DecimalMax 被标注的值必须不大于约束中指定的最大值. 这个约束的参数是一个通过BigDecimal定义的最大值的字符串表示.小数存在精度  
@DecimalMin 被标注的值必须不小于约束中指定的最小值. 这个约束的参数是一个通过BigDecimal定义的最小值的字符串表示.小数存在精度  
@Digits 验证 Number 和 String 的构成是否合法  
@Digits(integer=,fraction=) 验证字符串是否是符合指定格式的数字，interger指定整数精度，fraction指定小数精度。  
@Range(min=, max=) 被指定的元素必须在合适的范围内  
@Range(min=10000,max=50000,message=”range.bean.wage”)  
@Valid 递归的对关联对象进行校验, 如果关联对象是个集合或者数组,那么对其中的元素进行递归校验,如果是一个map,则对其中的值部分进行校验.(是否进行递归验证)  
@CreditCardNumber信用卡验证  
@Email 验证是否是邮件地址，如果为null,不进行验证，算通过验证。  
@ScriptAssert(lang= ,script=, alias=)  
@URL(protocol=,host=, port=,regexp=, flags=)  
____
