package com.rest.api.domain.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;

@Value
@With
@Builder
public class Product {
    Long id;
    String name;
    String description;
    BigDecimal price;
}
