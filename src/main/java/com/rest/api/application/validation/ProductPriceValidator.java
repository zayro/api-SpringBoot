package com.rest.api.application.validation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductPriceValidator {
    private static final BigDecimal MAX_PRICE = BigDecimal.valueOf(1_000_000L);

    public void validate(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Product price must be provided");
        }
        if (price.compareTo(MAX_PRICE) > 0) {
            throw new IllegalArgumentException("Product price cannot exceed " + MAX_PRICE);
        }
    }
}
