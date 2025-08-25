package com.booleanuk.cinema.controllers;

import com.booleanuk.cinema.models.Movie;
import com.booleanuk.cinema.models.Screening;
import com.booleanuk.cinema.payload.response.ErrorResponse;
import com.booleanuk.cinema.payload.response.Response;
import com.booleanuk.cinema.payload.response.ScreeningListResponse;
import com.booleanuk.cinema.payload.response.ScreeningResponse;
import com.booleanuk.cinema.repository.MovieRepository;
import com.booleanuk.cinema.repository.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("movies/{id}/screenings")
public class ScreeningController {

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping
    public ResponseEntity<ScreeningListResponse> getAllScreenings() {
        ScreeningListResponse screeningListResponse = new ScreeningListResponse();
        screeningListResponse.set(this.screeningRepository.findAll());
        return ResponseEntity.ok(screeningListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createScreening(@PathVariable int id, @RequestBody Screening screening) {
        Movie movie = this.movieRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found")
        );

        ScreeningResponse screeningResponse = new ScreeningResponse();
        try {
            screeningResponse.set(this.screeningRepository.save(screening));
            movie.addScreening(screening);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(screeningResponse, HttpStatus.CREATED);
    }

}
