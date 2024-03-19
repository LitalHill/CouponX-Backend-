package com.gateway.auth;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthFilter implements GatewayFilter {

    private final WebClient webClient;

    public AuthFilter(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        HttpHeaders headers = request.getHeaders();
        if (!headers.containsKey("token")) {
            return onError(exchange);
        }

        String token = request.getHeaders().getOrEmpty("token").get(0);

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") Map<String, String> body = new HashMap<>();
        body.put("token", token);

        return webClient
                .get()
                .uri("http://localhost:1337/parse-token")
                .headers(httpHeaders -> httpHeaders.addAll(request.getHeaders()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(ok -> {
                    if (ok) {
                        return chain.filter(exchange);
                    }
                    return onError(exchange);
                });
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
