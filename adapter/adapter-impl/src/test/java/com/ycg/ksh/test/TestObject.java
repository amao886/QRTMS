package com.ycg.ksh.test;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/20
 */

import com.ycg.ksh.adapter.api.MessageQueueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @TODO 类描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("local")
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestObject {

    @Resource
    private MessageQueueService messageQueueService;

    @Test
    public void testload(){

        //messageQueueService.sendMessage(QueueKeys.KYLIN_DATA_PUSH, new MediaMessage("com.asdsa.fgdf.xcvxc.Order", "111111111111111111111",  "消息测试"));


        // 收件人电子邮箱
        String to = "110686@ycgwl.com";

        // 发件人电子邮箱
        String from = "110686@ycgwl.com";

        // 指定发送邮件的主机
        /**
             服务器设置：
             发送服务器：smtp.263.net，ssl不加密，端口25；ssl加密，端口465。
             接收服务器：
             imap协议：imap.263.net，ssl不加密，端口143；ssl加密，端口993。
             pop协议：pop.263.net，ssl不加密，端口110；ssl加密，端口995。
         */
        String host = "smtp.263.net";

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(from, "123456"); //发件人邮件用户名、密码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from, "合同物流管理平台平台", "utf-8"));

            // Set To: 头部头字段//
            message.addRecipients(Message.RecipientType.TO, "110686@ycgwl.com,114211@ycgwl.com");

            // Set Subject: 头部头字段
            message.setSubject("This is the Subject Line!");

            // 设置消息体
            message.setText("This is actual message");

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....from runoob.com");
        }catch (Exception mex) {
            mex.printStackTrace();
        }
    }
}
