package com.ycg.ksh.adapter.impl.rabbitmq;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/17
 */

import com.rabbitmq.client.Channel;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.adapter.SysNetKey;
import com.ycg.ksh.entity.adapter.EmailMessage;
import com.ycg.ksh.entity.adapter.mq.QueueKeys;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.AbstractAdaptableMessageListener;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * mq消息接收监听
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/17
 */
public class QueueListenter extends AbstractAdaptableMessageListener {

    private static final String EMAIL_SENDER = "110686@ycgwl.com";
    private static final PasswordAuthentication AUTHENTICATION = new PasswordAuthentication(EMAIL_SENDER, "123456");

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Object object = getMessageConverter().fromMessage(message);
        if (object == null) {
            return;
        }
        handleMessage(RabbitmqUtil.messageType(message), RabbitmqUtil.messageKey(message), object);
    }

    public void handleMessage(String messageType, String messageKey, Object object) throws Exception{
        if(StringUtils.endsWithIgnoreCase(QueueKeys.MESSAGE_TYPE_EMAIL, messageType)){
            Globallys.executor(() -> {
                sendEmail((EmailMessage) object);
            });
        }
    }

    private void sendEmail(EmailMessage emailMessage){
        String net_email_exception_tos = SystemUtils.get(SysNetKey.NET_EMAIL_EXCEPTION_KEY);
        if(StringUtils.isNotBlank(net_email_exception_tos)){
            // 收件人电子邮箱
            //String to = "110686@ycgwl.com";

            // 发件人电子邮箱
            //String from = "110686@ycgwl.com";

            // 指定发送邮件的主机
            /*
             服务器设置：
             发送服务器：smtp.263.net，ssl不加密，端口25；ssl加密，端口465。
             接收服务器：
             imap协议：imap.263.net，ssl不加密，端口143；ssl加密，端口993。
             pop协议：pop.263.net，ssl不加密，端口110；ssl加密，端口995。
             */
            // 获取系统属性
            Properties properties = System.getProperties();
            // 设置邮件服务器
            properties.setProperty("mail.smtp.host", "smtp.263.net");
            properties.setProperty("mail.smtp.auth", "true");
            // 获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return AUTHENTICATION;
                }
            });
            try{
                // 创建默认的 MimeMessage 对象
                MimeMessage message = new MimeMessage(session);
                // Set From: 头部头字段
                message.setFrom(new InternetAddress(EMAIL_SENDER, "合同物流管理平台平台", "utf-8"));
                // Set To: 头部头字段//
                message.addRecipients(MimeMessage.RecipientType.TO, SystemUtils.get(SysNetKey.NET_EMAIL_EXCEPTION_KEY));
                //message.setReplyTo(new Address[]{new InternetAddress(EMAIL_SENDER)});
                // Set Subject: 头部头字段
                message.setSubject(emailMessage.getSubject());
                // 设置消息体
                message.setText(emailMessage.context());
                // 发送消息
                Transport.send(message);
            }catch (Exception mex) {
                mex.printStackTrace();
            }
        }
    }

}
