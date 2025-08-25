package com.booleanuk.cinema.repository;

import com.booleanuk.cinema.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
