package com.booleanuk.cinema.repository;

import com.booleanuk.cinema.models.ERole;
import com.booleanuk.cinema.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);

    boolean existsByName(ERole eRole);
}
