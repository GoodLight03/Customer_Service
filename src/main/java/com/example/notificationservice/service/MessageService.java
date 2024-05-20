package com.example.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.notificationservice.dto.MessageDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private EmailService emailService;

    @KafkaListener(id="notificationGroup", topics = "notification")
    public void listen(MessageDto messageDto){
        log.info("Received", messageDto.getTo());
        emailService.sendEmail(messageDto);
    }
}
