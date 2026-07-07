package com.rest.api.infrastructure.adapter.output.inmemory;

import com.rest.api.domain.model.Customer;
import com.rest.api.domain.port.CustomerRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {
    private final Map<String, Customer> customers = new ConcurrentHashMap<>();

    public InMemoryCustomerRepository() {
        customers.put("1001", new Customer("1001", "Ana Gómez", "ana@example.com", "ACTIVE"));
        customers.put("1002", new Customer("1002", "Luis Pérez", "luis@example.com", "ACTIVE"));
        customers.put("1003", new Customer("1003", "Marta Ruiz", "marta@example.com", "PENDING"));
    }

    @Override
    public Flux<Customer> findAll() {
        return Flux.fromIterable(customers.values());
    }

    @Override
    public Mono<Customer> findById(String id) {
        return Mono.justOrEmpty(customers.get(id));
    }
}
