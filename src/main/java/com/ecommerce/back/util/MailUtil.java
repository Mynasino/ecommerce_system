package com.ecommerce.back.util;

import com.ecommerce.back.exception.IllegalException;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    /**
     * QQ邮箱名
     */
    private static final String QQMAIL_Name = "1696781072@qq.com";
    /**
     * 邮箱对应的SMTP的授权码
     */
    private static final String QQMAIL_PASSWORD = "rhauhvgpsxfxeaha";

    private static final Session session;
    //初始化邮件会话
    static {
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");

        //SSL加密
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);

            properties.setProperty("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            logger.warn("SSL for mail failed " + e.getMessage());
        }

        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(QQMAIL_Name, QQMAIL_PASSWORD);
            }
        });
        session.setDebug(false);
    }

    /**
     * 发送邮件信息
     * @param toMailAddress 要发送的邮箱地址
     * @param subject 邮件标题
     * @param content 邮件内容
     * @throws MessagingException 邮件发送失败
     */
    public static void sendMailMessage(String toMailAddress, String subject, String content) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(QQMAIL_Name);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toMailAddress));
        message.setSubject(subject);
        message.setText(content);
        Transport.send(message);
    }

    /**
     * 检查邮箱地址格式
     * @param mailAddress 邮箱地址
     * @throws IllegalException 邮箱地址不合法
     */
    public static void checkMailAddLegality(String mailAddress) throws IllegalException {
        if (!mailAddress.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+"))
            throw new IllegalException("邮箱地址",mailAddress,"不合法");
    }
}
