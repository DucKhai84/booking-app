package com.devteria.api_gateway.service;

import com.devteria.api_gateway.dto.request.ApiResponse;
import com.devteria.api_gateway.dto.request.IntrospectRequest;
import com.devteria.api_gateway.dto.response.IntrospectResponse;
import com.devteria.api_gateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {

    IdentityClient identity_client;

    public Mono<ApiResponse<IntrospectResponse>> introspect (String token){
        return identity_client.introspect(IntrospectRequest.builder().token(token).build());
    }

}
