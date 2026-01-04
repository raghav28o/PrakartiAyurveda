package com.PrakartiAyurVeda.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private Integer age;

    private String gender;

    private String location;

    @Enumerated(EnumType.STRING)
    private FoodPreference foodPreference;
}
