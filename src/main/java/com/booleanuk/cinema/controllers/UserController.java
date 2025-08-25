package com.booleanuk.cinema.controllers;

import com.booleanuk.cinema.models.User;
import com.booleanuk.cinema.payload.response.ErrorResponse;
import com.booleanuk.cinema.payload.response.Response;
import com.booleanuk.cinema.payload.response.UserListResponse;
import com.booleanuk.cinema.payload.response.UserResponse;
import com.booleanuk.cinema.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers() {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser(@RequestBody User user) {
        UserResponse userResponse = new UserResponse();
        try {
            userResponse.set(this.userRepository.save(user));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getUserById(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        UserResponse userResponse = new UserResponse();
        userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody User user) {
        User userToUpdate = this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        userToUpdate.setName(user.getName());
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());
        // Update will not change password
        userToUpdate.setPhone(user.getPhone());

        try {
            userToUpdate = this.userRepository.save(userToUpdate);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(userToUpdate);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id) {
        User userToDelete = this.userRepository.findById(id).orElse(null);
        if (userToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.userRepository.delete(userToDelete);
        UserResponse userResponse = new UserResponse();
        userResponse.set(userToDelete);
        return ResponseEntity.ok(userResponse);
    }
}
