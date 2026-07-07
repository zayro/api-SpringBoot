package com.rest.api.infrastructure.adapter.input.web;

import com.rest.api.application.service.ProductRawService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductRawController {
    private final ProductRawService rawService;

    public ProductRawController(ProductRawService rawService) {
        this.rawService = rawService;
    }

    public record RawResponse(String sql, Object result) {}

    @GetMapping("/raw")
    public Mono<RawResponse> listRaw() {
        String sql = "SELECT id, name, description, price FROM products ORDER BY id";
        return rawService.findAllRaw().collectList().map(list -> new RawResponse(sql, list));
    }

    @GetMapping("/raw/{id}")
    public Mono<ResponseEntity<RawResponse>> getRaw(@PathVariable Long id) {
        String sql = "SELECT id, name, description, price FROM products WHERE id = :id";
        return rawService.findByIdRaw(id)
                .map(result -> ResponseEntity.ok(new RawResponse(sql, result)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/raw")
    public Mono<RawResponse> createRaw(@RequestBody ProductRequest req) {
        String sql = "INSERT INTO products (name, description, price) VALUES (:name, :description, :price) RETURNING id";
        return rawService.insertRaw(req.name(), req.description(), req.price())
                .map(id -> new RawResponse(sql, Map.of("id", id)));
    }

    @PutMapping("/raw/{id}")
    public Mono<RawResponse> updateRaw(@PathVariable Long id, @RequestBody ProductRequest req) {
        String sql = "UPDATE products SET name = :name, description = :description, price = :price WHERE id = :id";
        return rawService.updateRaw(id, req.name(), req.description(), req.price())
                .map(rows -> new RawResponse(sql, Map.of("rowsUpdated", rows)));
    }

    @DeleteMapping("/raw/{id}")
    public Mono<RawResponse> deleteRaw(@PathVariable Long id) {
        String sql = "DELETE FROM products WHERE id = :id";
        return rawService.deleteRaw(id).map(rows -> new RawResponse(sql, Map.of("rowsDeleted", rows)));
    }

    public record ProductRequest(String name, String description, BigDecimal price) {}
}
