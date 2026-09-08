package com.devteria.notification_service.repository.httpClient;

import com.devteria.notification_service.dto.request.EmailRequest;
import com.devteria.notification_service.dto.response.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name= "email-client", url= "${app.service.base_url}")
public interface EmailClient {

    @GetMapping("/v3/account")
    Object getAccount(@RequestHeader("api-key") String apiKey);

    @PostMapping(value = "${app.service.send_path}", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    EmailResponse sendMail(@RequestHeader("api-key") String apiKey, @RequestHeader("accept")String accept, @RequestBody EmailRequest body);
}
