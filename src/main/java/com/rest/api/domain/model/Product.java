package com.rest.api.domain.model;

import java.math.BigDecimal;

public final class Product {
    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;

    public Product(Long id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product withId(Long id) {
        return new Product(id, this.name, this.description, this.price);
    }
}
