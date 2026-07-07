package com.rest.api.infrastructure.adapter.input.web;

import com.rest.api.application.service.CustomerQueryService;
import com.rest.api.domain.model.Customer;
import com.rest.api.domain.port.CustomerRepository;
import com.rest.api.infrastructure.adapter.output.inmemory.InMemoryCustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CustomerController.class)
@Import(CustomerQueryService.class)
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void shouldReturnCustomers() {
        when(customerRepository.findAll()).thenReturn(Flux.just(
                new Customer("1001", "Ana Gómez", "ana@example.com", "ACTIVE")
        ));

        webTestClient.get()
                .uri("/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("1001")
                .jsonPath("$[0].name").isEqualTo("Ana Gómez");
    }

    @Test
    void shouldReturnNotFoundWhenCustomerDoesNotExist() {
        when(customerRepository.findById("9999")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/customers/9999")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
