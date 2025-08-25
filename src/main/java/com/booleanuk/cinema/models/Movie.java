package com.booleanuk.cinema.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String rating;

    @Column
    private int runtimeMins;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime createdAt;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private OffsetDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Screening> screenings;

    public Movie(String title, String rating, int runtimeMins, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.title = title;
        this.rating = rating;
        this.runtimeMins = runtimeMins;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Movie(int id) {
        this.id = id;
    }

    public void addScreening(Screening screening){
        this.screenings.add(screening);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
