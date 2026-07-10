package com.rest.api.infrastructure.adapter.output.r2dbc;

import com.rest.api.domain.model.Product;
import com.rest.api.domain.port.ProductRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Profile("r2dbc")
public class ProductRepositoryR2dbcAdapter implements ProductRepository {

    private final ProductR2dbcRepository repo;
    private final com.rest.api.infrastructure.mapper.ProductMapper mapper;

    public ProductRepositoryR2dbcAdapter(ProductR2dbcRepository repo, com.rest.api.infrastructure.mapper.ProductMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }


    @Override
    public Flux<Product> findAll() {
        return repo.findAll().map(mapper::toDomain);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Product> save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        // ensure id null for create
        if (entity.getId() == null) entity.setId(null);
        return repo.save(entity).map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repo.deleteById(id);
    }
}
