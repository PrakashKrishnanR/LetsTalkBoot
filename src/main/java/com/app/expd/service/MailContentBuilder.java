package com.app.expd.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    public String build(String message){
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate", context);
    }

    public String buildPostNotification(String message){
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("postNotification", context);
    }

    public String buildResetPasswordNotification(String message){
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("resetPassword", context);
    }

}
