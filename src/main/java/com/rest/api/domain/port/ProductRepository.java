package com.rest.api.domain.port;

import com.rest.api.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Flux<Product> findAll();
    Mono<Product> findById(Long id);
    Mono<Product> save(Product product);
    Mono<Void> deleteById(Long id);
}
