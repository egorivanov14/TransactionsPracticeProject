package org.example.service.implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("$spring.mail.username")
    private String fromEmail;

    public MailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine){
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendWelcomeMessage(String email, String userName) {
        try{

            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("date", LocalDate.now());

            String html = templateEngine.process("welcome", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Добро пожаловать в BudgetTracker!");
            helper.setText(html, true);

            javaMailSender.send(message);
        }catch (MessagingException e){
            log.error("Failed to send message to {}" ,email);
        }
    }
}
