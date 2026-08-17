package com.devteria.identity.repository.httpClient;

import com.devteria.identity.dto.request.ProfileCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.awt.*;

@FeignClient(name = "profile-service",url = "${app.services.profile}")
public interface ProfileClient {
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    Object createProfile(@RequestBody ProfileCreationRequest request);

}
