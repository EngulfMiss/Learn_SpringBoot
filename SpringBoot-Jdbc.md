# JDBC
### JDBC操作
- 导入相关的依赖
```xml
<!-- jdbc -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<!-- Mysql -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

- 配置数据源
```yml
spring:
  datasource:
    username: root
    password: 52snowgnar
    url: jdbc:mysql://localhost:3306/ssmbuild?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    driver-class-name: com.mysql.cj.jdbc.Driver
```

- 测试数据源
```java
@SpringBootTest
class Spring04DataApplicationTests {
    @Autowired
    private DataSource dataSource;
    @Test
    void contextLoads() throws SQLException {
        //查看默认数据源
        System.out.println(dataSource.getClass());
        //获取数据库连接
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        //释放资源
        connection.close();
    }
}
```

- CRUD(伪)
```java
@RestController
public class JDBCController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //查询数据库的所有信息 万能MAP
    @GetMapping("/books")
    public List<Map<String,Object>> bookList(){
        String sql = "select * from books";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql);
        return list_maps;
    }

    @GetMapping("/addBook")
    public String addBook(){
        String sql = "insert into books(bookID,bookName,bookCounts,detail) values(6,'JavaWeb',20,'javaweb入门')";
        jdbcTemplate.update(sql);
        return "Add,OK";
    }

    @GetMapping("/updateBook/{id}")
    public String updateBook(@PathVariable("id") int id){
        String sql = "update books set bookName = ?,bookCounts = ? where bookID = "+id;
        Object[] obj = new Object[2];
        obj[0] = "JavaWeb";
        obj[1] = "10";
        jdbcTemplate.update(sql,obj);
        return "update,OK";
    }

    @GetMapping("/delBook/{id}")
    public String delBook(@PathVariable("id") int id){
        String sql = "delete from books where bookID = ?";
        jdbcTemplate.update(sql,id);
        return "Del,OK";
    }
}
```

## Druid数据源
- 导入依赖
```xml
<!-- Druid -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.6</version>
</dependency>
```

### Druid后台监控
- Druid有许多专属配置 有些是监控需要用到的
```xml
spring:
  datasource:
    username: root
    password: 52snowgnar
    url: jdbc:mysql://localhost:3306/ssmbuild?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource

    # Druid专有配置
    # 下面为连接池的补充设置，应用到上面所有数据源中
    # 初始化大小，最小，最大
    initial-size: 5
    min-idle: 5
    max-active: 20
    # 配置获取连接等待超时的时间
    max-wait: 60000
    # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    time-between-eviction-runs-millis: 60000
    # 配置一个连接在池中最小生存的时间，单位是毫秒
    min-evictable-idle-time-millis: 300000
    validation-query: SELECT 1 FROM DUAL
    test-while-idle: true
    test-on-borrow: false
    test-on-return: false
#    # 打开PSCache，并且指定每个连接上PSCache的大小
#    pool-prepared-statements: true
#    #   配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
#    max-pool-prepared-statement-per-connection-size: 20
    filters: stat,wall,log4j
    use-global-data-source-stat: true
#    # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
    connect-properties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
#    # 配置监控服务器
#    stat-view-servlet:
#      login-username: admin
#      login-password: 123456
#      reset-enable: false
#      url-pattern: /druid/*
#      # 添加IP白名单
#      #allow:
#      # 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
#      #deny:
#    web-stat-filter:
#      # 添加过滤规则
#      url-pattern: /*
#      # 忽略过滤格式
#      exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"
```

- 编写一个Druid配置类(记得交给springboot管理)
```java
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
```

## springboot整合mybatis
- 启动器
```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.0</version>
</dependency>
```

- 编写连接数据库的配置文件
```properties
spring.datasource.username=root
spring.datasource.password=52snowgnar
spring.datasource.url=jdbc:mysql://localhost:3306/ssmbuild?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

- 编写pojo实体类
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Books {
    private Integer bookID;
    private String bookName;
    private Integer bookCounts;
    private String detail;
}
```

- 编写mapper
    - mapper接口上添加@Mapper  
      或者
    - 启动类上添加@MapperScan
```java
@Mapper
@Repository
public interface BookMapper {
    //查询所有书籍
    List<Books> selectBookList();

    //通过id查询书籍
    Books selectBookById(Integer id);

    //添加书籍
    int addBook(Books books);

    //修改书籍
    int updateBook(Books books);

    //删除书籍
    int delBook(Integer id);
}
```
    

- 编写mapper映射文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.engulf.mapper.BookMapper">
    <select id="selectBookList" resultType="com.engulf.pojo.Books">
        select * from ssmbuild.books
    </select>

    <select id="selectBookById" resultType="books" parameterType="int">
        select * from ssmbuild.books where bookID = #{id}
    </select>

    <insert id="addBook" parameterType="books">
        insert into ssmbuild.books(bookID, bookName, bookCounts, detail) VALUES (#{bookID},#{bookName},#{bookCounts},#{detail})
    </insert>

    <update id="updateBook" parameterType="books">
        update ssmbuild.books set bookName = #{bookName},bookCounts = #{bookCounts},detail = #{detail} where bookID = #{bookID}
    </update>

    <delete id="delBook" parameterType="int">
        delete from ssmbuild.books where detail = #{id}
    </delete>
</mapper>
```

- springboot配置文件中整合mybatis
```xml
# 整合mybatis
# 要起别名的包
mybatis.type-aliases-package=com.engulf.pojo
# 映射文件位置
mybatis.mapper-locations=classpath:mybatis/mapper/*.xml
```
