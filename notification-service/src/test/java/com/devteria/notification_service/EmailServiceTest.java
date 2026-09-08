package com.devteria.notification_service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.devteria.notification_service.service.EmailService;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.lang.reflect.Field;

@SpringBootTest
public class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @Test
    public void testApiKey() throws Exception {
        Field field = EmailService.class.getDeclaredField("api_key");
        field.setAccessible(true);
        String apiKey = (String) field.get(emailService);
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        
        String body = "{\"sender\":{\"name\":\"Bookteria\",\"email\":\"booking-dev-company@gmail.com\"},\"to\":[{\"name\":\"Khai Ho\",\"email\":\"hokhai84@gmail.com\"}],\"subject\":\"test\",\"htmlContent\":\"test\"}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange("https://api.brevo.com/v3/smtp/email", HttpMethod.POST, entity, String.class);
            System.out.println("REST_SUCCESS: " + response.getBody());
        } catch (Exception e) {
            System.out.println("REST_ERROR: " + e.getMessage());
        }
    }
}
