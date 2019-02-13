package com.ecommerce.back.util;

import com.sun.mail.util.MailSSLSocketFactory;
import org.junit.Test;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtilTest {
    //@Test
    public void sendMailMessageTest() throws Exception {
        MailUtil.sendMailMessage("854008160@qq.com","test","test-content");
    }
}
