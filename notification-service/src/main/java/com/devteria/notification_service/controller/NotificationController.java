package com.devteria.notification_service.controller;

import com.devteria.event.dto.NotificationEvent;
import com.devteria.notification_service.dto.request.Recipient;
import com.devteria.notification_service.dto.request.SendEmailRequest;
import com.devteria.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    EmailService email_service;

    @KafkaListener(topics = "notification-delivery", groupId = "notification-group")
    public void listen(NotificationEvent message){
        email_service.sendMail(SendEmailRequest.builder()
                        .to(Recipient.builder()
                                .email(message.getRecipient())
                                .build())
                        .subject(message.getSubject())
                        .htmlContent(message.getBody())
                .build());
    }
}
