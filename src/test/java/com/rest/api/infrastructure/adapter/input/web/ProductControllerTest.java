package com.rest.api.infrastructure.adapter.input.web;

import com.rest.api.application.service.ProductService;
import com.rest.api.domain.model.Product;
import com.rest.api.infrastructure.adapter.input.web.ProductController;
import com.rest.api.infrastructure.adapter.input.web.ProductController.ProductRequest;
import com.rest.api.infrastructure.adapter.input.web.ProductController.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductControllerTest {
  
    private WebTestClient webTestClient;
    private ProductService productService;
    
    @BeforeEach
    void setup() {
        productService = mock(ProductService.class);
        webTestClient = WebTestClient.bindToController(new ProductController(productService))
                .build();
    }
    
    @Test
    void testListProducts() {
        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("High-performance laptop")
                .price(new BigDecimal("1200.50"))
                .build();
        
        when(productService.list()).thenReturn(Flux.just(product));
        
        webTestClient.get()
                .uri("/api/products")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponse.class)
                .hasSize(1)
                .contains(new ProductResponse(1L, "Laptop", "High-performance laptop", new BigDecimal("1200.50")));
    }
    
    @Test
    void testGetProductById() {
        Product product = Product.builder()
                .id(1L)
                .name("Mouse")
                .description("Wireless mouse")
                .price(new BigDecimal("45.00"))
                .build();
        
        when(productService.get(1L)).thenReturn(Mono.just(product));
        
        webTestClient.get()
                .uri("/api/products/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(new ProductResponse(1L, "Mouse", "Wireless mouse", new BigDecimal("45.00")));
    }
    
    @Test
    void testGetProductByIdNotFound() {
        when(productService.get(999L)).thenReturn(Mono.empty());
        
        webTestClient.get()
                .uri("/api/products/999")
                .exchange()
                .expectStatus().isNotFound();
    }
    
    @Test
    void testCreateProduct() {
        Product product = Product.builder()
                .id(2L)
                .name("Monitor")
                .description("4K Ultra HD")
                .price(new BigDecimal("599.99"))
                .build();
        
        when(productService.create(any(Product.class))).thenReturn(Mono.just(product));
        
        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductRequest("Monitor", "4K Ultra HD", new BigDecimal("599.99")))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductResponse.class)
                .isEqualTo(new ProductResponse(2L, "Monitor", "4K Ultra HD", new BigDecimal("599.99")));
    }
    
    @Test
    void testCreateProductWithInvalidPrice() {
        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductRequest("Monitor", "4K", new BigDecimal("1000001")))
                .exchange()
                .expectStatus().isBadRequest();
    }
    
    @Test
    void testCreateProductWithBlankName() {
        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductRequest("", "Description", new BigDecimal("100.00")))
                .exchange()
                .expectStatus().isBadRequest();
    }
    
    @Test
    void testUpdateProduct() {
        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Updated Laptop")
                .description("Updated description")
                .price(new BigDecimal("1100.00"))
                .build();
        
        when(productService.update(eq(1L), any(Product.class))).thenReturn(Mono.just(updatedProduct));
        
        webTestClient.put()
                .uri("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductRequest("Updated Laptop", "Updated description", new BigDecimal("1100.00")))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductResponse.class)
                .isEqualTo(new ProductResponse(1L, "Updated Laptop", "Updated description", new BigDecimal("1100.00")));
    }
    
    @Test
    void testUpdateProductNotFound() {
        when(productService.update(eq(999L), any(Product.class))).thenReturn(Mono.empty());
        
        webTestClient.put()
                .uri("/api/products/999")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductRequest("Name", "Description", new BigDecimal("100.00")))
                .exchange()
                .expectStatus().isNotFound();
    }
    
    @Test
    void testDeleteProduct() {
        when(productService.delete(1L)).thenReturn(Mono.empty());
        
        webTestClient.delete()
                .uri("/api/products/1")
                .exchange()
                .expectStatus().isNoContent();
    }
    
    @Test
    void testListProductsV1Endpoint() {
        Product product = Product.builder()
                .id(1L)
                .name("Keyboard")
                .description("Mechanical keyboard")
                .price(new BigDecimal("150.00"))
                .build();
        
        when(productService.list()).thenReturn(Flux.just(product));
        
        webTestClient.get()
                .uri("/api/v1/products")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductResponse.class)
                .hasSize(1);
    }
}