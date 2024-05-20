package com.example.notificationservice.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.notificationservice.dto.MessageDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

public interface EmailService {
    void sendEmail(MessageDto messageDto);
}

@Service
@Slf4j
class EmailServiceImpl implements EmailService{


    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Async
    public void sendEmail(MessageDto messageDto) {
        try {
            log.info("Start.... Sending email");
            MimeMessage mess = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mess,StandardCharsets.UTF_8.name());

            Context context=new Context();
            context.setVariable("name", messageDto.getToName());
            context.setVariable("content", messageDto.getContent());
            String html=templateEngine.process("wwelcomeeamil", context);

            mimeMessageHelper.setTo(messageDto.getTo());
            mimeMessageHelper.setText(html,true);
            mimeMessageHelper.setSubject(messageDto.getSubject());
            mimeMessageHelper.setFrom(from);

            javaMailSender.send(mess);

            log.info("Emd... email sent success");

        } catch (MessagingException e) {
            log.info("Email sent with error: "+e.getMessage());
        }
        
    }
    
}
