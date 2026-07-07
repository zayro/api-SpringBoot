package com.rest.api.domain.port;

import com.rest.api.domain.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository {
    Flux<Customer> findAll();

    Mono<Customer> findById(String id);
}
