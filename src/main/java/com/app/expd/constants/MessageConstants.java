package com.app.expd.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageConstants {

    @Value("${host.name}")
    private  String hostName;

    @Value("${auth.service.url}")
    private  String authServiceUrl;

    @Value("${frontend.post.url}")
    private String frontEndPostUrl;

    private   String MailAlert = "Please activate your account by clicking the below link";
    private   String MessageBody = "Thank you for Signing up to Let'sTalk " +
            "Please Click on the Below Link to activate yourAccount : ";

    private String commentNotification = " has commented on your Post";

    private String commentLink = "\n To Check on the comment goto :- \n";

    private String resetPassword = "Your Password Reset Completed Successfully: \n  " +
            "Your New Password: ";

    private String resetSubject = "LetsTalk Password reset ";
}
