package com.invoicesystem.apigateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;


@Component
public class RedisRateLimiterFilter implements GlobalFilter, Ordered {
    private final ReactiveStringRedisTemplate redisTemplate;
    private final Duration windowDuration = Duration.ofMinutes(1);
    private final long limit = 100;


    public RedisRateLimiterFilter(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String ip = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                .map(address -> address.getAddress().getHostAddress())
                .orElse("unknown");
        String key = "rate:limit:" + ip;

        return redisTemplate.opsForValue().increment(key)
                .flatMap(currentCount -> {
                    if (currentCount == 1) {
                        redisTemplate.expire(key, windowDuration).subscribe();
                    }
                    if (currentCount > limit) {
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        return exchange.getResponse().setComplete();
                    } else {
                        return chain.filter(exchange);
                    }
                });
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

