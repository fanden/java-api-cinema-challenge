package com.booleanuk.cinema.controllers;

import com.booleanuk.cinema.models.Customer;
import com.booleanuk.cinema.models.Screening;
import com.booleanuk.cinema.models.Ticket;
import com.booleanuk.cinema.payload.response.ErrorResponse;
import com.booleanuk.cinema.payload.response.Response;
import com.booleanuk.cinema.payload.response.TicketListResponse;
import com.booleanuk.cinema.payload.response.TicketResponse;
import com.booleanuk.cinema.repository.CustomerRepository;
import com.booleanuk.cinema.repository.ScreeningRepository;
import com.booleanuk.cinema.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("customers/{customerId}/screenings/{screeningId}")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @GetMapping
    public ResponseEntity<TicketListResponse> getAllTickets(@PathVariable int customerId, @PathVariable int screeningId){
        TicketListResponse ticketListResponse = new TicketListResponse();

        List<Ticket> tickets = new ArrayList<>();

                this.ticketRepository.findAll().stream().filter(
                        ticket ->
                                ticket.getCustomer().getId() == customerId && ticket.getScreening().getId() == screeningId)
                        .forEach(ticket -> tickets.add(ticket));

        ticketListResponse.set(tickets);
        return ResponseEntity.ok(ticketListResponse);
    }

    //TODO rewrite
    @PostMapping
    public ResponseEntity<Response<?>> createTicket(@PathVariable int customerId, @PathVariable int screeningId, @RequestBody Ticket ticket) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer with that id were found"));

        Screening screening = screeningRepository.findById(screeningId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No screening with that id were found"));

        ticket.setCustomer(customer);
        ticket.setScreening(screening);

        TicketResponse ticketResponse = new TicketResponse();
        try{
            ticketResponse.set(this.ticketRepository.save(ticket));
            customer.addTicket(ticket);
            screening.addTicket(ticket);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ticketResponse, HttpStatus.CREATED);
    }
}
