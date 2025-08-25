package com.booleanuk.cinema.repository;

import com.booleanuk.cinema.models.ERole;
import com.booleanuk.cinema.models.Role;
import com.booleanuk.cinema.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
