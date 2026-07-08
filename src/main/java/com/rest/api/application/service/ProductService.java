package com.rest.api.application.service;

import com.rest.api.application.validation.ProductPriceValidator;
import com.rest.api.domain.model.Product;
import com.rest.api.domain.port.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final ProductPriceValidator validator;

    public ProductService(ProductRepository repository, ProductPriceValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Flux<Product> list() {
        return repository.findAll();
    }

    public Mono<Product> get(Long id) {
        return repository.findById(id);
    }

    public Mono<Product> create(Product p) {
        validator.validate(p.getPrice());
        return repository.save(p.withId(null));
    }

    public Mono<Product> update(Long id, Product p) {
        validator.validate(p.getPrice());
        return repository.findById(id)
                .flatMap(existing -> repository.save(p.withId(id)));
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}
