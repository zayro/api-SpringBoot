package com.rest.api.infrastructure.adapter.input.web;

import com.rest.api.application.service.CustomerQueryService;
import com.rest.api.domain.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerQueryService customerQueryService;

    public CustomerController(CustomerQueryService customerQueryService) {
        this.customerQueryService = customerQueryService;
    }

    @GetMapping
    public Flux<CustomerResponse> getAllCustomers() {
        return customerQueryService.getAllCustomers()
                .map(CustomerResponse::fromDomain);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponse>> getCustomer(@PathVariable String id) {
        return customerQueryService.getCustomer(id)
                .map(CustomerResponse::fromDomain)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public record CustomerResponse(String id, String name, String email, String status) {
        public static CustomerResponse fromDomain(Customer customer) {
            return new CustomerResponse(customer.id(), customer.name(), customer.email(), customer.status());
        }
    }
}
