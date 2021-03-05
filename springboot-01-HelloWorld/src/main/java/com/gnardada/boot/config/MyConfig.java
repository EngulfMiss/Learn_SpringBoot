package com.gnardada.boot.config;

import com.gnardada.boot.domain.Partner;
import com.gnardada.boot.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1.配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单实例的
 * 2.配置类自己本身也是组件
 * 3.springboot2,@Configuration中的新属性proxyBeanMethods:代理bean的方法
 *      Full(proxyBeanMethods = true)
 *      Lite(proxyBeanMethods = false)
 *      解决组件依赖问题
 *      没有组件依赖的话，建议设置为false
 */
@Configuration(proxyBeanMethods = false) //告诉SpringBoot这是一个配置类 == 配置文件
public class MyConfig {
    /**
     * 外部无论对配置类中的这个组件注册方法调用多少次，获取的都是之前注册到容器中的实例对象
     * @return
     */
    @Bean //给容器添加组件。以方法明作为组件的id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public User user01(){
        User QSJ = new User("QSJ",23);
        //proxyBeanMethods = true ,User组件依赖了myPartner组件
        QSJ.setPartner(myPartner());
        return QSJ;
    }

    @Bean("myPart")
    public Partner myPartner(){
        return new Partner("Kindred");
    }
}
