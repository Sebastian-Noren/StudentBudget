package se.hkr.studentbudget.login;

import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailUtil {

    public static void send(String to, String subject, String msg) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.enable", "true");

        final String fromEmail = "Studentbudget.noreply@gmail.com";
        final String fromPwd = "Studentbudg1";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPwd);
            }
        });

        Message message = prepareMsg(session, fromEmail, to, subject, msg);


        Transport.send(message);


    }

    private static Message prepareMsg(Session session, String fromEmail, String toEmail, String subject, String msg) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(msg);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OBOY");
        }
        return null;
    }


}
