package com.booleanuk.cinema.controllers;

import com.booleanuk.cinema.models.Movie;
import com.booleanuk.cinema.payload.response.ErrorResponse;
import com.booleanuk.cinema.payload.response.MovieListResponse;
import com.booleanuk.cinema.payload.response.MovieResponse;
import com.booleanuk.cinema.payload.response.Response;
import com.booleanuk.cinema.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping
    public ResponseEntity<MovieListResponse> getAllMovies() {
        MovieListResponse movieListResponse = new MovieListResponse();
        movieListResponse.set(this.movieRepository.findAll());
        return ResponseEntity.ok(movieListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createMovie(@RequestBody Movie movie) {
        MovieResponse movieResponse = new MovieResponse();
        try {
            movieResponse.set(this.movieRepository.save(movie));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(movieResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateMovie (@PathVariable int id, @RequestBody Movie movie) {
        Movie movieToUpdate = this.movieRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found")
        );

        movieToUpdate.setUpdatedAt(OffsetDateTime.now());

        MovieResponse movieResponse = new MovieResponse();
        movieResponse.set(this.movieRepository.save(movieToUpdate));
        return new ResponseEntity<>(movieResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteMovie(@PathVariable int id) {
        Movie movieToDelete = this.movieRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found")
        );

        this.movieRepository.delete(movieToDelete);
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.set(movieToDelete);
        return new ResponseEntity<>(movieResponse, HttpStatus.CREATED);
    }

}
