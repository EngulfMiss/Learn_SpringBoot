package com.engulf;

import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.File;

@SpringBootTest
@SuppressWarnings("all")
class Springboot09TaskApplicationTests {

    @Autowired
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

        mimeMessageHelper.setTo(new String[]{"1216982545@qq.com"});
        mimeMessageHelper.setFrom("1216982545@qq.com");

        mailSender.send(mimeMessage);
    }


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

}
