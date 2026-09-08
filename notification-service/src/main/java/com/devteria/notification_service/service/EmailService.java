package com.devteria.notification_service.service;

import com.devteria.notification_service.dto.request.EmailRequest;
import com.devteria.notification_service.dto.request.SendEmailRequest;
import com.devteria.notification_service.dto.request.Sender;
import com.devteria.notification_service.dto.response.EmailResponse;
import com.devteria.notification_service.exception.AppException;
import com.devteria.notification_service.exception.ErrorCode;
import com.devteria.notification_service.repository.httpClient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal= true)
@Slf4j
public class EmailService {
    EmailClient email_client;

    @NonFinal
    @Value("${app.service.api-key}")
    String api_key;

    public Object getBrevoAccount() {
        try {
            Object account = email_client.getAccount(api_key);

            log.info("Brevo account response: {}", account);

            return account;
        } catch (FeignException e) {
            log.error("Brevo status: {}", e.status());
            log.error("Brevo response body: {}", e.contentUTF8());
            throw e;
        }
    }

    public EmailResponse sendMail(SendEmailRequest request){
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Dev-Company")
                        .email("creatorstesting84@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .htmlContent(request.getHtmlContent())
                .subject(request.getSubject())
                .build();
        try {
           return email_client.sendMail(api_key, MediaType.APPLICATION_JSON_VALUE, emailRequest);
        }catch(FeignException e){
            throw e;
        }
    }
}
