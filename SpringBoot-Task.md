# 任务
## 异步任务
- 在你的异步方法上添加@Async注解
```java
@Service
public class AsyncService {
    //告诉Spring这是一个异步的方法
    @Async
    public void hello(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据正在处理......");
    }
}
```

- 在启动类上开启异步支持
```java
@SpringBootApplication
@EnableAsync   //开启异步功能
public class Springboot09TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(Springboot09TaskApplication.class, args);
    }
}
```
## 邮件任务
- 导入依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

- 编写邮件发送代码
```java
@Autowired
//MailSenderAutoConfiguration中查看源码
private JavaMailSenderImpl mailSender;

//发送一个简单的邮件
@Test
void contextLoads() {
    //一个简单的邮件
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setSubject("To 凯shy不打碟QAQ");  //邮件标题
    simpleMailMessage.setText("我已经一天没听到您说您5杀的故事了!");  //邮件内容

//        simpleMailMessage.setTo("1216982545@qq.com");
    simpleMailMessage.setTo("1226850814@qq.com");
//        simpleMailMessage.setTo("1258874357@qq.com");
    simpleMailMessage.setFrom("1216982545@qq.com");

    mailSender.send(simpleMailMessage);
}


//发送一个复杂的邮件
@Test
void contextLoads2() throws MessagingException {
    //一个复杂邮件
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    //组装~
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"utf-8");
    mimeMessageHelper.setSubject("这是标题");
    mimeMessageHelper.setText("<p style='color:green'>setText(text, true),第二个参数为true表示开启html解析</p>",true);

    //附件
    mimeMessageHelper.addAttachment("顶风作案.jpg",new File("C:\\Users\\Lenovo\\Desktop\\1.jpg"));

    mimeMessageHelper.setTo(new String[]{"1216982545@qq.com","1258874357@qq.com","1258874357@qq.com"});
    mimeMessageHelper.setFrom("1216982545@qq.com");

    mailSender.send(mimeMessage);
}
```
- 根据自己的需求可以封装一个简单的工具类
```java
/**
 * @author Engulf迷失
 * @param htmlSupport 是否支持html解析
 * @param title 邮件标题
 * @param text  邮件文本内容
 * @param formWho 谁发的
 * @param toWho 发给谁               
 * @throws MessagingException
 */
public void sendMailUtil(Boolean htmlSupport,String title,String text,String formWho,String... toWho) throws MessagingException {
    //一个复杂邮件
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    //组装~
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"utf-8");
    mimeMessageHelper.setSubject(title);
    mimeMessageHelper.setText(text,htmlSupport);

    //附件
    mimeMessageHelper.addAttachment("顶风作案.jpg",new File("C:\\Users\\Lenovo\\Desktop\\1.jpg"));

    mimeMessageHelper.setTo(toWho);
    mimeMessageHelper.setFrom(formWho);

    mailSender.send(mimeMessage);
}
```
