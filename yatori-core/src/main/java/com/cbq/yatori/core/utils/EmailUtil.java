package com.cbq.yatori.core.utils;

import com.cbq.yatori.core.entity.Config;
import com.cbq.yatori.core.entity.Setting;
import com.cbq.yatori.core.entity.User;
import com.cbq.yatori.core.utils.ConfigUtils;
import okhttp3.internal.http2.Settings;
import com.cbq.yatori.core.entity.EmailInform;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class EmailUtil {
    private static final EmailInform EmailInform =ConfigUtils.loadingConfig().getSetting().getEmailInform();

    public static void sendEmail(String Username, String CourseName) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", EmailInform.getSmtpHost());
        props.put("mail.smtp.port", EmailInform.getSmtpPort());
        String subject="Yatori课程助手提醒";
        String ToMail=EmailInform.getEmail();
        String Content="您的账号："+Username+"已经刷完："+CourseName+"，请注意查看！";
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailInform.getEmail(), EmailInform.getPassword());
            }
        };

        Session session = Session.getInstance(props, auth);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(EmailInform.getEmail()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(ToMail));
        message.setSubject(subject);

        // 如果是纯文本
        message.setText(Content);

        // 如果是HTML格式
        // MimeBodyPart textPart = new MimeBodyPart();
        // textPart.setContent(bodyHtml, "text/html; charset=UTF-8");
        // MimeMultipart multipart = new MimeMultipart();
        // multipart.addBodyPart(textPart);
        // message.setContent(multipart);

        Transport.send(message);
    }
}