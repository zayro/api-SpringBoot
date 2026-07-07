package com.rest.api.infrastructure.mapper;

import com.rest.api.domain.model.Product;
import com.rest.api.infrastructure.adapter.output.r2dbc.ProductEntity;
import org.springframework.stereotype.Component;
import io.r2dbc.spi.Row;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductMapper {

    public Product toDomain(ProductEntity e) {
        if (e == null) return null;
        return Product.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .price(e.getPrice())
                .build();
    }

    public ProductEntity toEntity(Product p) {
        if (p == null) return null;
        return new ProductEntity(p.getId(), p.getName(), p.getDescription(), p.getPrice());
    }

    public Product rowToProduct(Row row) {
        return Product.builder()
                .id(row.get("id", Long.class))
                .name(row.get("name", String.class))
                .description(row.get("description", String.class))
                .price(row.get("price", BigDecimal.class))
                .build();
    }

    public Map<String, Object> rowToMap(Row row) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", row.get("id", Long.class));
        m.put("name", row.get("name", String.class));
        m.put("description", row.get("description", String.class));
        m.put("price", row.get("price", BigDecimal.class));
        return m;
    }

    public Product fromMap(Map<String, Object> map) {
        if (map == null) return null;
        return Product.builder()
                .id((Long) map.get("id"))
                .name((String) map.get("name"))
                .description((String) map.get("description"))
                .price((BigDecimal) map.get("price"))
                .build();
    }
}
