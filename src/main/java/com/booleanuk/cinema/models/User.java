package com.booleanuk.cinema.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name= "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
    })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    @Size(min = 1, max = 20)
    @NotBlank
    private String username;

    @Column
    @Size(min = 1, max = 50)
    @NotBlank
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;

    @Column
    @Size(min = 1, max = 20)
    @NotBlank
    private String phone;

    @ManyToMany(fetch = FetchType.LAZY)
    // TODO there's 100% gonna be some fuckery here I gotta fix up later. Generally watch out for variable names
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String name, String username, String email, String password, String phone) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public User(int id){
        this.id = id;
    }

}
