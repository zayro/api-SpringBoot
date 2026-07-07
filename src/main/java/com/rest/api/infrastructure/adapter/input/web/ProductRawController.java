package com.rest.api.infrastructure.adapter.input.web;

import com.rest.api.application.service.ProductRawService;
import com.rest.api.domain.model.Product;
import com.rest.api.infrastructure.adapter.input.web.ProductController.ProductResponse;
import com.rest.api.infrastructure.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRawController {
    private final ProductRawService rawService;
    private final ProductMapper mapper;

    public ProductRawController(ProductRawService rawService, ProductMapper mapper) {
        this.rawService = rawService;
        this.mapper = mapper;
    }

    @GetMapping("/raw")
    public Mono<ProductRawResponse> listRaw() {
        String sql = "SELECT id, name, description, price FROM products ORDER BY id";
        return rawService.findAllRaw()
                .collectList()
                .map(products -> products.stream().map(ProductResponse::fromDomain).toList())
                .map(response -> new ProductRawResponse(sql, response));
    }

    @GetMapping("/raw/{id}")
    public Mono<ResponseEntity<ProductRawResponse>> getRaw(@PathVariable Long id) {
        String sql = "SELECT id, name, description, price FROM products WHERE id = :id";
        return rawService.findByIdRaw(id)
                .map(ProductResponse::fromDomain)
                .map(product -> ResponseEntity.ok(new ProductRawResponse(sql, product)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/raw")
    public Mono<ProductRawResponse> createRaw(@RequestBody ProductRequest req) {
        String sql = "INSERT INTO products (name, description, price) VALUES (:name, :description, :price) RETURNING id";
        return rawService.insertRaw(req.name(), req.description(), req.price())
                .map(id -> new ProductRawResponse(sql, new CreateResult(id)));
    }

    @PutMapping("/raw/{id}")
    public Mono<ProductRawResponse> updateRaw(@PathVariable Long id, @RequestBody ProductRequest req) {
        String sql = "UPDATE products SET name = :name, description = :description, price = :price WHERE id = :id";
        return rawService.updateRaw(id, req.name(), req.description(), req.price())
                .map(rows -> new ProductRawResponse(sql, new RowsResult("rowsUpdated", rows)));
    }

    @DeleteMapping("/raw/{id}")
    public Mono<ProductRawResponse> deleteRaw(@PathVariable Long id) {
        String sql = "DELETE FROM products WHERE id = :id";
        return rawService.deleteRaw(id)
                .map(rows -> new ProductRawResponse(sql, new RowsResult("rowsDeleted", rows)));
    }

    public record ProductRequest(String name, String description, BigDecimal price) {}

    public record CreateResult(Long id) {}

    public record RowsResult(String key, Long value) {}
}
