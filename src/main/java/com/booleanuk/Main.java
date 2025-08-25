package com.booleanuk;

import com.booleanuk.cinema.models.ERole;
import com.booleanuk.cinema.models.Role;
import com.booleanuk.cinema.models.User;
import com.booleanuk.cinema.repository.RoleRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        if (!this.roleRepository.existsByName(ERole.ROLE_CUSTOMER)) {
            this.roleRepository.save(new Role(ERole.ROLE_CUSTOMER));
        }

        if (!this.roleRepository.existsByName(ERole.ROLE_MANAGER)) {
            this.roleRepository.save(new Role(ERole.ROLE_MANAGER));
        }

        if (!this.roleRepository.existsByName(ERole.ROLE_ADMIN)) {
            this.roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
    }
}
