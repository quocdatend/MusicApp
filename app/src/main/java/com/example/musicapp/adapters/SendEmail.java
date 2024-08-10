package com.example.musicapp.adapters;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail extends AsyncTask<Void, Void, Void>{
    // Mailtrap SMTP configuration
    private String to;
    private String subject;
    private String message;

    public SendEmail(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
    public SendEmail() {
    }

    @Override
    protected Void doInBackground(Void... params) {
        sendEmail();
        return null;
    }

    public void sendEmail() {
        String TAG = "SmtpEmailSender";
        String SMTP_HOST = "smtp.gmail.com";
        int SMTP_PORT = 587;
        String SMTP_USERNAME = "hnqdat2003@gmail.com";
        String SMTP_PASSWORD = "yoav fpqr pjwe uagk";
        String EMAIL_FROM = "hnqdat2003@gmail.com";
        String EMAIL_TO = this.to;
        String EMAIL_SUBJECT = this.subject;
        String EMAIL_BODY = this.message;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(EMAIL_TO));
            message.setSubject(EMAIL_SUBJECT);
            message.setText(EMAIL_BODY);

            Transport.send(message);

            Log.d(TAG, "Email sent successfully!");
        } catch (MessagingException e) {
            Log.e(TAG, "Error sending email: " + e.getMessage());
        }
    }

    public int generateResetCode() {
        Random random = new Random();
        return random.nextInt(900000) + 100000; // Generate a random 6-digit number
    }

    // hash sha256 string
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error hashing password: " + e.getMessage());
            return null;
        }
    }

}
