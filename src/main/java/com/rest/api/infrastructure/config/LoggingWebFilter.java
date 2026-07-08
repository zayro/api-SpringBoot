package com.rest.api.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class LoggingWebFilter implements WebFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggingWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long start = System.currentTimeMillis();
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();

        log.info("Incoming request: {} {}", method, path);

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> log.info("Response for {} {} completed in {} ms",
                        method, path, System.currentTimeMillis() - start))
                .doOnError(error -> log.error("Error for {} {} after {} ms",
                        method, path, System.currentTimeMillis() - start, error));
    }
}
