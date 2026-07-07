package com.rest.api.infrastructure.adapter.output.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductR2dbcRepository extends R2dbcRepository<ProductEntity, Long> {
}
