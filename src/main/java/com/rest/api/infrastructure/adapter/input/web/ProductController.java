package com.rest.api.infrastructure.adapter.input.web;

import com.rest.api.application.service.ProductService;
import com.rest.api.domain.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ProductResponse> list() {
        return service.list().map(ProductResponse::fromDomain);
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<ProductResponse>> get(@PathVariable Long id) {
        return service.get(id)
                .map(ProductResponse::fromDomain)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponse> create(@Valid @RequestBody ProductRequest req) {
        Product p = req.toDomain();
        return service.create(p).map(ProductResponse::fromDomain);
    }

    @PutMapping(value = "/{id}")
    public Mono<ResponseEntity<ProductResponse>> update(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
        Product p = req.toDomain();
        return service.update(id, p)
                .map(ProductResponse::fromDomain)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return service.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    public record ProductRequest(String name, String description, BigDecimal price) {
        public Product toDomain() {
            return new Product(null, name, description, price);
        }
    }

    public record ProductResponse(Long id, String name, String description, BigDecimal price) {
        public static ProductResponse fromDomain(Product p) {
            return new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice());
        }
    }
}
