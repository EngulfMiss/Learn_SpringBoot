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
