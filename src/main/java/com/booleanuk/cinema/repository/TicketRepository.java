package com.booleanuk.cinema.repository;

import com.booleanuk.cinema.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
