package com.rest.api.application.service;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductRawService {

    private final DatabaseClient db;

    public ProductRawService(DatabaseClient db) {
        this.db = db;
    }

    public Flux<Map<String, Object>> findAllRaw() {
        String sql = "SELECT id, name, description, price FROM products ORDER BY id";
        return db.sql(sql)
                .map((row, metadata) -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", row.get("id", Long.class));
                    m.put("name", row.get("name", String.class));
                    m.put("description", row.get("description", String.class));
                    m.put("price", row.get("price", BigDecimal.class));
                    return m;
                }).all();
    }

    public Mono<Map<String, Object>> findByIdRaw(Long id) {
        String sql = "SELECT id, name, description, price FROM products WHERE id = :id";
        return db.sql(sql)
                .bind("id", id)
                .map((row, metadata) -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", row.get("id", Long.class));
                    m.put("name", row.get("name", String.class));
                    m.put("description", row.get("description", String.class));
                    m.put("price", row.get("price", BigDecimal.class));
                    return m;
                }).one();
    }

    public Mono<Long> insertRaw(String name, String description, BigDecimal price) {
        String sql = "INSERT INTO products (name, description, price) VALUES (:name, :description, :price) RETURNING id";
        return db.sql(sql)
                .bind("name", name)
                .bind("description", description)
                .bind("price", price)
                .map((row, meta) -> row.get("id", Long.class))
                .one();
    }

    public Mono<Long> updateRaw(Long id, String name, String description, BigDecimal price) {
        String sql = "UPDATE products SET name = :name, description = :description, price = :price WHERE id = :id";
        return db.sql(sql)
                .bind("name", name)
                .bind("description", description)
                .bind("price", price)
                .bind("id", id)
                .fetch()
                .rowsUpdated();
    }

    public Mono<Long> deleteRaw(Long id) {
        String sql = "DELETE FROM products WHERE id = :id";
        return db.sql(sql)
                .bind("id", id)
                .fetch()
                .rowsUpdated();
    }
}
