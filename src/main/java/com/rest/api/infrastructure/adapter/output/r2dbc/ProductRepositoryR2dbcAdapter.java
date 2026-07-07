package com.rest.api.infrastructure.adapter.output.r2dbc;

import com.rest.api.domain.model.Product;
import com.rest.api.domain.port.ProductRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class ProductRepositoryR2dbcAdapter implements ProductRepository {

    private final ProductR2dbcRepository repo;

    public ProductRepositoryR2dbcAdapter(ProductR2dbcRepository repo) {
        this.repo = repo;
    }

    private Product toDomain(ProductEntity e) {
        if (e == null) return null;
        return new Product(e.getId(), e.getName(), e.getDescription(), e.getPrice());
    }

    private ProductEntity toEntity(Product p) {
        if (p == null) return null;
        return new ProductEntity(p.getId(), p.getName(), p.getDescription(), p.getPrice());
    }

    @Override
    public Flux<Product> findAll() {
        return repo.findAll().map(this::toDomain);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override
    public Mono<Product> save(Product product) {
        ProductEntity entity = toEntity(product);
        // ensure id null for create
        if (entity.getId() == null) entity.setId(null);
        return repo.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repo.deleteById(id);
    }
}
