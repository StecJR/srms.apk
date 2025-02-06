package com.stec.srms.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.stec.srms.interfaces.OnEmailSentListener;

import java.io.InputStream;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailHandler {
    private static String getTemplate(Context context, String file) {
        try {
            InputStream inputStream = context.getAssets().open(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e("Class:EmailHandler:getTemplate", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    private static boolean sendMail(Context context, String receiver, String subject, String body) {
        EnvVariable.loadEnvVariable(context);
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", EnvVariable.get("SMTP_HOST"));
            properties.put("mail.smtp.port", EnvVariable.get("SMTP_PORT"));
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
            properties.put("mail.smtp.ssl.trust", EnvVariable.get("SMTP_HOST"));

            String SENDER_EMAIL = EnvVariable.get("SMTP_EMAIL_SENDER");
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            SENDER_EMAIL,
                            EnvVariable.get("SMTP_EMAIL_PASSWORD")
                    );
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "SRMS"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setReplyTo(InternetAddress.parse(SENDER_EMAIL));
            message.addHeader("X-Priority", "1");
            message.addHeader("X-Mailer", "JavaMail");
            message.setHeader("X-Originating-IP", InetAddress.getLocalHost().getHostAddress());
            message.setHeader("Disposition-Notification-To", SENDER_EMAIL);
            message.addHeader("Content-Type", "text/html; charset=utf-8");
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
            return true;
        } catch (SendFailedException e) {
            Toast.authenticationError(context, "Wrong email address");
            return false;
        } catch (Exception e) {
            Log.e("EmailHandler:sendSignupOTPMail", Objects.requireNonNull(e.getMessage()), e);
            return false;
        }
    }

    public static void sendSignupOTPMail(Context context, String receiverMail, String name, int otp, OnEmailSentListener listener) {
        LoadingScreen.start(context, "Sending email");
        new Thread(() -> {
            String subject = "SRMS: Verify Your Email";
            String mailBody = getTemplate(context, "signupOtpTemplate.html");
            if (mailBody != null) {
                mailBody = mailBody.replace("{{NAME}}", name);
                mailBody = mailBody.replace("{{OTP}}", String.valueOf(otp));
            }

            boolean success = sendMail(context, receiverMail, subject, mailBody);
            new Handler(Looper.getMainLooper()).post(() -> {
                LoadingScreen.stop();
                if (listener != null) listener.onEmailSent(success);
            });
        }).start();
    }
}
/*
 */