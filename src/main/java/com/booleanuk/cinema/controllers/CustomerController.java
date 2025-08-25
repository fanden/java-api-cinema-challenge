package com.booleanuk.cinema.controllers;

import com.booleanuk.cinema.models.Customer;
import com.booleanuk.cinema.payload.response.CustomerListResponse;
import com.booleanuk.cinema.payload.response.CustomerResponse;
import com.booleanuk.cinema.payload.response.ErrorResponse;
import com.booleanuk.cinema.payload.response.Response;
import com.booleanuk.cinema.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<CustomerListResponse> getAllCustomers() {
        CustomerListResponse customerListResponse = new CustomerListResponse();
        customerListResponse.set(this.customerRepository.findAll());
        return ResponseEntity.ok(customerListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createCustomer(@RequestBody Customer customer) {
        CustomerResponse customerResponse = new CustomerResponse();
        try {
            customerResponse.set(this.customerRepository.save(customer));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateCustomer (@PathVariable int id, @RequestBody Customer customer) {
        Customer customerToUpdate = this.customerRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")
        );

        customerToUpdate.setUpdatedAt(OffsetDateTime.now());

        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.set(this.customerRepository.save(customerToUpdate));
        return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteCustomer(@PathVariable int id) {
        Customer customerToDelete = this.customerRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")
        );

        this.customerRepository.delete(customerToDelete);
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.set(customerToDelete);
        return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
    }
}
