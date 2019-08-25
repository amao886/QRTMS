package com.ycg.ksh.test;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/20
 */

import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.http.HttpClient;
import com.ycg.ksh.common.system.SystemUtils;

import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

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
    private static final String SMS_CODE_STRING = "您的验证码是 %s，请在对应页面提交验证码完成验证。若不是本人操作，请忽略";//模板请勿修改

    public static final String SMS_USERNAME = "shslsswl";
    public static final String SMS_PASSWORD = "UDteYkTV";
    public static final String SMS_SEND_URL = "http://userinterface.vcomcn.com/Opration.aspx";//短信发送
    public static final String SMS_YE_URL = "http://userinterface.vcomcn.com/GetResult.aspx";//短信获取余额
    
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
    
    public static String randomCode(int count) {
        StringBuilder vcode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            vcode.append((int) (Math.random() * 9));
        }
        return vcode.toString();
    }
    
    public static String MD5(String encryptContent) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(encryptContent.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
        	System.out.println("发送短信异常 {}");
            throw new BusinessException("短信密码加密异常");
        }
        return result;
    }
    
    private static String createSubmitXml(String mobile, String content) {
        StringBuffer sp = new StringBuffer();
        sp.append(String.format("<Group Login_Name=\"%s\" Login_Pwd=\"%s\" InterFaceID=\"0\" OpKind=\"0\">",
        		SMS_USERNAME, MD5(SMS_PASSWORD)));
        sp.append(String.format("<E_Time>%s</E_Time>", ""));
        sp.append("<Item>");
        sp.append("<Task>");
        sp.append("<Recive_Phone_Number>" + mobile+ "</Recive_Phone_Number>");
        sp.append("<Content><![CDATA[" + content + "]]></Content>");
        sp.append("<Search_ID>1</Search_ID>");
        sp.append("</Task>");
        sp.append("</Item>");
        sp.append("</Group>");

        return sp.toString();
    }
    
    private static void send(String mobile, String content) {
        HttpClient httpClient = HttpClient.createHttpClient(SMS_SEND_URL,HttpClient.Type.POST);
        httpClient.property("ContentType", ContentType.create("text/xml"));
        httpClient.property("charset", "GB2312");
    	httpClient.setParameterString(content);
        try {
            String result = httpClient.post();
            if (!"00".equals(result)) {
            	throw new BusinessException("发送短信异常: " + result);
            }
        } catch (Exception e) {
            throw new BusinessException("发送短信异常");
        }
	}
    
    public static String sendCode(String mobile, String template) {
    	String code = randomCode(9);
    	send(mobile, createSubmitXml(mobile, String.format(template, code)));
    	return code;
    }
    
    public static void main(String[] args) {
    	System.out.println(sendCode("15056066683", SMS_CODE_STRING));
	}
}
