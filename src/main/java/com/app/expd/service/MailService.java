package com.app.expd.service;

import com.app.expd.exceptions.LetsTalkException;
import com.app.expd.models.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendNotification(NotificationEmail notificationEmail) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("letsTalkdemo@email.com");
            messageHelper.setTo(notificationEmail.getRecipent());
            messageHelper.setSubject(notificationEmail.getSubject());
            String content = mailContentBuilder.build(notificationEmail.getBody());
            messageHelper.setText(content, true);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Activation Mail Sent");
        }
        catch (Exception e){
            System.out.println(e);
            throw new LetsTalkException("Exception Occured while sending Mail to " + notificationEmail.getRecipent() );

        }
    }


    @Async
    public void sendCommentNotification(NotificationEmail notificationEmail) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(notificationEmail.getRecipent());
            messageHelper.setSubject(notificationEmail.getSubject());
            String content = mailContentBuilder.buildPostNotification(notificationEmail.getBody());
            messageHelper.setText(content, true);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Notification Mail Sent");
        }
        catch (Exception e){
            System.out.println(e);
            throw new LetsTalkException("Exception Occured while sending Mail to " + notificationEmail.getRecipent() );

        }
    }

    public void resetPasswordNotification(NotificationEmail notificationEmail) {

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(notificationEmail.getRecipent());
            messageHelper.setSubject(notificationEmail.getSubject());
            String content = mailContentBuilder.buildResetPasswordNotification(notificationEmail.getBody());
            messageHelper.setText(content, true);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Notification Mail Sent");
        }
        catch (Exception e){
            System.out.println(e);
            throw new LetsTalkException("Exception Occured while sending Mail to " + notificationEmail.getRecipent() );

        }
    }
}
