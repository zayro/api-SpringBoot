package com.rest.api.application.service;

import com.rest.api.domain.model.Product;
import com.rest.api.domain.port.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Flux<Product> list() {
        return repository.findAll();
    }

    public Mono<Product> get(Long id) {
        return repository.findById(id);
    }

    public Mono<Product> create(Product p) {
        return repository.save(p.withId(null));
    }

    public Mono<Product> update(Long id, Product p) {
        return repository.findById(id)
                .flatMap(existing -> repository.save(p.withId(id)));
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}
