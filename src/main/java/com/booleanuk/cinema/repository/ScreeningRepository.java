package com.booleanuk.cinema.repository;

import com.booleanuk.cinema.models.Role;
import com.booleanuk.cinema.models.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<Screening, Integer> {
}
