package com.rest.api.infrastructure.adapter.output.inmemory;

import com.rest.api.domain.model.Product;
import com.rest.api.domain.port.ProductRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile({"dev", "test", "inmemory"})
public class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> map = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(100);

    public InMemoryProductRepository() {
        var p1 = Product.builder().id(1L).name("Lapicero").description("Lapicero azul").price(new BigDecimal("1.50")).build();
        var p2 = Product.builder().id(2L).name("Cuaderno").description("Cuaderno A4").price(new BigDecimal("3.20")).build();
        map.put(p1.getId(), p1);
        map.put(p2.getId(), p2);
        seq.set(2);
    }

    @Override
    public Flux<Product> findAll() {
        return Flux.fromIterable(map.values());
    }

    @Override
    public Mono<Product> findById(Long id) {
        return Mono.justOrEmpty(map.get(id));
    }

    @Override
    public Mono<Product> save(Product product) {
        if (product.getId() == null) {
            long id = seq.incrementAndGet();
            Product created = product.withId(id);
            map.put(id, created);
            return Mono.just(created);
        }
        map.put(product.getId(), product);
        return Mono.just(product);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        map.remove(id);
        return Mono.empty();
    }
}
