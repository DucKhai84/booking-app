package com.devteria.notification_service.controller;


import com.devteria.notification_service.dto.request.ApiResponse;
import com.devteria.notification_service.dto.request.SendEmailRequest;
import com.devteria.notification_service.dto.response.EmailResponse;
import com.devteria.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    EmailService email_service;

    @GetMapping("/brevo-account")
    public Object getBrevoAccount() {
        return email_service.getBrevoAccount();
    }

    @PostMapping("/email/send")
    ApiResponse<EmailResponse> sendMail(@RequestBody SendEmailRequest request){
        return ApiResponse.<EmailResponse>builder()
                .result(email_service.sendMail(request))
                .build();
    }

    @KafkaListener(topics = "onboard-successful", groupId = "notification-group")
    public void listen(String message){
        log.info("Message Receive: {}", message);
    }
}
