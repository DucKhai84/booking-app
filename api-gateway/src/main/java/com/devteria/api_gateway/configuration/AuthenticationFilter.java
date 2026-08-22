package com.devteria.api_gateway.configuration;

import com.devteria.api_gateway.dto.request.ApiResponse;
import com.devteria.api_gateway.repository.IdentityClient;
import com.devteria.api_gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter,Ordered {
    IdentityClient identityClient;
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    String[] publicEndpoint = {
            "/identity/auth/.*",
            "/identity/users/register"
    };

    @Value("${app.api-prefix}")
    @NonFinal
    private String api_prefix;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(isPublicEndpoint(exchange.getRequest())){
            return chain.filter(exchange);
        }
         List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

         if(CollectionUtils.isEmpty(authHeaders))
             return unauthenticated(exchange.getResponse());

         String token = authHeaders.getFirst().replace("Bearer", "");

         return identityService.introspect(token).flatMap(introspectResponseApiResponse -> {
             if(introspectResponseApiResponse.getResult().isValid()){
                return chain.filter(exchange);
             }else{
                 return unauthenticated(exchange.getResponse());
             }
         }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    private boolean isPublicEndpoint(ServerHttpRequest request){
        return Arrays.stream(publicEndpoint)
                .anyMatch(s -> request.getURI().getPath().matches(api_prefix + s));
    }


    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated (ServerHttpResponse response){

        ApiResponse<?> apiResponse = ApiResponse.builder().code(1401).message("Unauthenticated").build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }

        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
