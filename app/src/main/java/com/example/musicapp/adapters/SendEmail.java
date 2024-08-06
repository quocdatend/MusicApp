package com.example.musicapp.adapters;

import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {
    // Mailtrap SMTP configuration
    String to;
    String subject;
    String message;

    public SendEmail(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;

    }

    public void sendEmail() {
        String host = "live.smtp.mailtrap.io";
        String port = "587";
        String username = "hnqdat2003@gmail.com";
        String password = "yoav fpqr pjwe uagk";
        // Email properties
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);

        // Authenticating with Mailtrap
        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Compose the message
            MimeMessage messages = new MimeMessage(session);
            messages.setFrom(new InternetAddress(username));
            messages.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to)); // Multiple recipients
            messages.setSubject(subject);

            // Create multipart object for email content
            Multipart emailContent = new MimeMultipart();

            // HTML Text
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(message, "text/html");
            emailContent.addBodyPart(htmlPart);

            // Attachment
//            MimeBodyPart attachmentPart = new MimeBodyPart();
//            attachmentPart.attachFile("/path/to/file"); // Specify the file path
//            emailContent.addBodyPart(attachmentPart);

            // Set content
            messages.setContent(emailContent);

            // Send the email
            Transport.send(messages);

//            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
