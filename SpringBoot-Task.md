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

## 定时任务
- TaskScheduler 任务调度程序
- TaskExecutor 任务执行者
- @EnableScheduling 开启定时功能的注解
- @Scheduled 什么时候执行

使用：
- 启动类中开启定时任务
```java
@SpringBootApplication
@EnableScheduling  //开启定时功能的注解
public class Springboot09TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot09TaskApplication.class, args);
    }

}
```

- 给定时执行的方法添加@Scheduled() 参数写cron表达式
**Cron表达式**  
// 秒 分 时 日 月 周几
```java
@Service
public class ScheduledService {
    //我们想在一个特定的时间执行这段代码
    // 秒 分 时 日 月 周几
    /*
        30 15 10 * * ?   每天10点15分30秒
     */
    @Scheduled(cron = "0 * * * * ?")
    public void hello(){
        System.out.println("Hello,你被执行了~");
    }
}
```
______
**Cron表达式是一个字符串，字符串以5或6个空格隔开，分为6或7个域，每一个域代表一个含义，Cron有如下两种语法格式：**  
- Seconds Minutes Hours DayofMonth Month DayofWeek Year
或者  
- Seconds Minutes Hours DayofMonth Month DayofWeek

每一个域都使用数字，但还可以出现如下特殊字符，它们的含义是：  
(1)*：表示匹配该域的任意值，假如在Minutes域使用*, 即表示每分钟都会触发事件。

(2)?:只能用在DayofMonth和DayofWeek两个域。它也匹配域的任意值，但实际不会。因为DayofMonth和 DayofWeek会相互影响。例如想在每月的20日触发调度，不管20日到底是星期几，则只能使用如下写法： 13 13 15 20 * ?, 其中最后一位只能用？，而不能使用*，如果使用*表示不管星期几都会触发，实际上并不是这样。

(3)-:表示范围，例如在Minutes域使用5-20，表示从5分到20分钟每分钟触发一次

(4)/：表示起始时间开始触发，然后每隔固定时间触发一次，例如在Minutes域使用5/20,则意味着5分钟触发一次，而25，45等分别触发一次.

(5),:表示列出枚举值值。例如：在Minutes域使用5,20，则意味着在5和20分每分钟触发一次。

(6)L:表示最后，只能出现在DayofWeek和DayofMonth域，如果在DayofWeek域使用5L,意味着在最后的一个星期四触发。

(7)W: 表示有效工作日(周一到周五),只能出现在DayofMonth域，系统将在离指定日期的最近的有效工作日触发事件。例如：在 DayofMonth使用5W，如果5日是星期六，则将在最近的工作日：星期五，即4日触发。如果5日是星期天，则在6日(周一)触发；如果5日在星期一 到星期五中的一天，则就在5日触发。另外一点，W的最近寻找不会跨过月份

(8)LW:这两个字符可以连用，表示在某个月最后一个工作日，即最后一个星期五。

(9)#:用于确定每个月第几个星期几，只能出现在DayofMonth域。例如在4#2，表示某月的第二个星期三。
______

cron表达式在线编辑网址：(http://www.bejson.com/othertools/cron/)[http://www.bejson.com/othertools/cron/]

______
常用表达式例子

  （1）0/2 * * * * ?   表示每2秒 执行任务

  （1）0 0/2 * * * ?    表示每2分钟 执行任务

  （1）0 0 2 1 * ?   表示在每月的1日的凌晨2点调整任务

  （2）0 15 10 ? * MON-FRI   表示周一到周五每天上午10:15执行作业

  （3）0 15 10 ? 6L 2002-2006   表示2002-2006年的每个月的最后一个星期五上午10:15执行作

  （4）0 0 10,14,16 * * ?   每天上午10点，下午2点，4点 

  （5）0 0/30 9-17 * * ?   朝九晚五工作时间内每半小时 

  （6）0 0 12 ? * WED    表示每个星期三中午12点 

  （7）0 0 12 * * ?   每天中午12点触发 

  （8）0 15 10 ? * *    每天上午10:15触发 

  （9）0 15 10 * * ?     每天上午10:15触发 

  （10）0 15 10 * * ?    每天上午10:15触发 

  （11）0 15 10 * * ? 2005    2005年的每天上午10:15触发 

  （12）0 * 14 * * ?     在每天下午2点到下午2:59期间的每1分钟触发 

  （13）0 0/5 14 * * ?    在每天下午2点到下午2:55期间的每5分钟触发 

  （14）0 0/5 14,18 * * ?     在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发 

  （15）0 0-5 14 * * ?    在每天下午2点到下午2:05期间的每1分钟触发 

  （16）0 10,44 14 ? 3 WED    每年三月的星期三的下午2:10和2:44触发 

  （17）0 15 10 ? * MON-FRI    周一至周五的上午10:15触发 

  （18）0 15 10 15 * ?    每月15日上午10:15触发 

  （19）0 15 10 L * ?    每月最后一日的上午10:15触发 

  （20）0 15 10 ? * 6L    每月的最后一个星期五上午10:15触发 

  （21）0 15 10 ? * 6L 2002-2005   2002年至2005年的每月的最后一个星期五上午10:15触发 

  （22）0 15 10 ? * 6#3   每月的第三个星期五上午10:15触发
______
