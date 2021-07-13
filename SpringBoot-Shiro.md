# Shiro
## quickstart
- 导入依赖

```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
    <version>1.7.1</version>
</dependency>

<!-- configure logging -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jcl-over-slf4j</artifactId>
    <version>1.7.31</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.31</version>
</dependency>
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```
- shiro配置文件
```ini
[users]
# user 'root' with password 'secret' and the 'admin' role
root = secret, admin
# user 'guest' with the password 'guest' and the 'guest' role
guest = guest, guest
# user 'presidentskroob' with password '12345' ("That's the same combination on
# my luggage!!!" ;)), and role 'president'
presidentskroob = 12345, president
# user 'darkhelmet' with password 'ludicrousspeed' and roles 'darklord' and 'schwartz'
darkhelmet = ludicrousspeed, darklord, schwartz
# user 'lonestarr' with password 'vespa' and roles 'goodguy' and 'schwartz'
lonestarr = vespa, goodguy, schwartz

# -----------------------------------------------------------------------------
# Roles with assigned permissions
#
# Each line conforms to the format defined in the
# org.apache.shiro.realm.text.TextConfigurationRealm#setRoleDefinitions JavaDoc
# -----------------------------------------------------------------------------
[roles]
# 'admin' role has all permissions, indicated by the wildcard '*'
admin = *
# The 'schwartz' role can do anything (*) with any lightsaber:
schwartz = lightsaber:*
# The 'goodguy' role is allowed to 'drive' (action) the winnebago (type) with
# license plate 'eagle5' (instance specific id)
goodguy = winnebago:drive:eagle5
```

- Quickstart.java
```java
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Simple Quickstart application showing how to use Shiro's API.
 *
 * @since 0.9 RC2
 */
public class Quickstart {

    private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);


    public static void main(String[] args) {

        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        IniRealm iniRealm = new IniRealm("classpath:shiro.ini");
        securityManager.setRealm(iniRealm);

        SecurityUtils.setSecurityManager(securityManager);

        // Now that a simple Shiro environment is set up, let's see what you can do:

        // get the currently executing user:
        // 获取当前的用户对象Subject
        Subject currentUser = SecurityUtils.getSubject();

        // Do some stuff with a Session (no need for a web or EJB container!!!)
        // 通过当前用户拿到Session
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("Retrieved the correct value! [" + value + "]");
        }

        // let's login the current user so we can check against roles and permissions:
        // 判断当前用户是否被认证
        if (!currentUser.isAuthenticated()) {
            // token：令牌
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            token.setRememberMe(true);  //设置记住我
            try {
                currentUser.login(token);  //执行登录操作
            } catch (UnknownAccountException uae) {
                log.info("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            }
        }

        //say who they are:
        //print their identifying principal (in this case, a username):
        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //test a role:
        if (currentUser.hasRole("schwartz")) {
            log.info("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }

        //粗粒度
        //test a typed permission (not instance-level)
        if (currentUser.isPermitted("lightsaber:wield")) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        //细粒度
        //a (very powerful) Instance Level permission:
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        //all done - log out!
        //注销
        currentUser.logout();

        //结束
        System.exit(0);
    }
}
```

____
- 一些核心方法
    - Subject currentUser = SecurityUtils.getSubject();  --- 获取当前的用户对象Subject
    - Session session = currentUser.getSession();  --- 通过当前用户拿到Session
    - currentUser.isAuthenticated()  --- 判断当前用户是否被认证
    - Subject的一些方法
        - currentUser.getPrincipal()  --- 获取当前用户信息
        - currentUser.hasRole("schwartz")  --- 用户是否有这个角色
        - currentUser.isPermitted("lightsaber:wield")  --- 用户是否有权限
        - currentUser.logout();  --- 注销
       
____      

## SpringBoot集成Shiro
**Shiro三大核心对象**
- Subject：用户
- SecurityManager：管理所有用户
- Realm：连接数据

____
- 导入依赖
```xml
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-web-starter</artifactId>
    <version>1.7.1</version>
</dependency>
```

- 编写一个realm对象(继承AuthorizingRealm类)
```java
import com.engulf.pojo.User;
import com.engulf.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class ChampionRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了==>授权doGetAuthorizationInfo");

        SimpleAuthorizationInfo Info = new SimpleAuthorizationInfo();
//        Info.addStringPermission("user:add");
        //拿到当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        //获取用户
        User currentUser = (User)subject.getPrincipal();     //取的就是下面传过来的user，SimpleAuthenticationInfo(user,user.getPassword(),""); 使得授权和认证关联

        //设置用户权限
        Info.addStringPermission(currentUser.getPerms());

        return Info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了==>认证doGetAuthenticationInfo");

        // 用户名，密码,数据库中获取
        UsernamePasswordToken userTaken = (UsernamePasswordToken)authenticationToken;

        User user = userService.getUserByName(userTaken.getUsername());

        if(!userTaken.getUsername().equals(user.getUsername())){  //判断用户名是否正确
            return null;  //return null 就会抛出UnknownAccountException
        }
        //密码认证shiro自己完成,并加密
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
```

- 编写配置类
**ShiroFilterFactoryBean的beanName一定要叫shiroFilterFactoryBean**
```java
@Configuration
public class ShiroConfig {
    //3.ShiroFilterFactoryBean
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager securityManager){
    ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
    //设置安全管理器
    bean.setSecurityManager(securityManager);

    //添加shiro的内置过滤器
    /*
        anon：无需认证就可以访问
        authc：必须认证了才能访问
        user：必须拥有 记住我 功能才能用
        perms：拥有对某个资源的权限才能访问
        role：拥有某个角色权限才能访问
     */
    // public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap)
    Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
    
    //  授权拦截操作要在登录拦截之前
    //  授权拦截操作要在登录拦截之前
    //  授权拦截操作要在登录拦截之前
    //  授权拦截操作(访问什么请求需要什么权限)，正常情况下，检测出没有权限后跳转到未授权页面
        filterChainDefinitionMap.put("/champion/add","perms[user:add]");
        filterChainDefinitionMap.put("/champion/update","perms[user:update]");
    
    //登录拦截
    // filterChainDefinitionMap.put("/champion/add","anon");
    //设置什么请求，怎么访问  例如,请求/champion/add 无需认证就可以访问
//        filterChainDefinitionMap.put("/champion/add","authc");
//        filterChainDefinitionMap.put("/champion/update","authc");
    filterChainDefinitionMap.put("/champion/*","authc");
    bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

    //设置登录请求
    bean.setLoginUrl("/toLogin");
    
    //设置未授权请求
    bean.setUnauthorizedUrl("/Unauthorized");

    return bean;


    //2.DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("championRealm") ChampionRealm championRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联管理realm
        securityManager.setRealm(championRealm);
        return securityManager;
    }


    //1.创建realm对象，需要自定义
    @Bean
    public ChampionRealm championRealm(){
        return new ChampionRealm();
    }
}
```

**认证**
____
- 写一个controller负责跳转到登录页面
```java
@RequestMapping("/toLogin")
public String toLogin(){
    return "login";
}
```

- 在配置类中开启登录页(承接上方配置使用)
```java
bean.setLoginUrl("/toLogin");  //设置登录请求
```

- 需要自己写登录页面
登录页面(将登录信息写入表单，跳转至controller进行shiro的登录验证)
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h2>登录</h2>
    <p th:text="${msg}" style="color: red;"></p>
    <form th:action="@{/login}" method="post">
        <p>用户名:<input type="text" name="username"></p>
        <p>密 码:<input type="password" name="password"></p>
        <input type="submit" value="提交">
    </form>
</body>
</html>
```

- 在controller中获取shiro登录用户信息
```java
@RequestMapping("/login")
public String login(String username,String password,Model model){
    //获取当前的用户
    Subject subject = SecurityUtils.getSubject();
    //封装用户的登录数据
    UsernamePasswordToken token = new UsernamePasswordToken(username, password);
    try {
        subject.login(token);  //执行登录的方法
        return "redirect:index";
    }catch (UnknownAccountException uae){
        model.addAttribute("msg","用户名错误");
        return "login";
    }catch (IncorrectCredentialsException ice) { //密码错误
        model.addAttribute("msg","密码错误");
        return "login";
    }
}
```

- shiro会自动到realm对象中去认证(认证代码片段)
```java
//认证
@Override
protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
    System.out.println("执行了==>认证doGetAuthenticationInfo");

    // 用户名，密码,数据库中获取
    String name = "kindred";
    String password = "52snowgnar";

    UsernamePasswordToken userTaken = (UsernamePasswordToken)authenticationToken;
    if(!userTaken.getUsername().equals(name)){  //判断用户名是否正确
        return null;  //return null 就会抛出UnknownAccountException
    }
    //密码认证shiro自己完成

    return new SimpleAuthenticationInfo(user,password,"");
}
```

- 登录后访问页面需要对应权限，权限检测
```java
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了==>授权doGetAuthorizationInfo");

        SimpleAuthorizationInfo Info = new SimpleAuthorizationInfo();
//        Info.addStringPermission("user:add");
        //拿到当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        //获取用户
        User currentUser = (User)subject.getPrincipal();     /*取的就是从认证处理传过来的user，SimpleAuthenticationInfo(user,user.getPassword(),""); 使得授权和认证关联*/

        //设置用户权限
        Info.addStringPermission(currentUser.getPerms());

        return Info;
    }
```
