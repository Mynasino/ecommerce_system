package com.ecommerce.back.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailTest1 {
    public static void main(String[] args) throws Exception {
        /*
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.qq.com");
        mailSender.setProtocol("smtp");
        mailSender.setUsername("854008160@qq.com");
        //TODO hide password
        mailSender.setPassword("tjumicmyimowbdgi");
        mailSender.setDefaultEncoding("UTF-8");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("854008160@qq.com");
        message.setTo("854008160@qq.com");
        message.setSubject("subject");
        message.setText("text");

        mailSender.send(message);

        Integer i = new Integer(1);
        i = i + 1;*/

        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(properties);
        session.setDebug(true);
        Transport transport = session.getTransport();
        transport.connect("854008160@qq.com", "tjumicmyimowbdgi");

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("854008160@qq.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("854008160@qq.com"));
        message.setSubject("subject");
        message.setContent("你好啊", "text/html;charset=UTF-8");

        transport.sendMessage(message, message.getAllRecipients());
    }
}
