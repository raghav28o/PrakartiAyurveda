package com.PrakartiAyurVeda.diet.entity;

import java.time.LocalDate;

import com.PrakartiAyurVeda.common.enums.DoshaType;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name = "diet_plans")
@Data
public class DietPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long assessmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoshaType doshaType;

    @Column(nullable = false)
    private String breakfast;

    @Column(nullable = false)
    private String lunch;

    @Column(nullable = false)
    private String dinner;

    private String avoidFoods;

    @Column(nullable = false)
    private LocalDate createdDate = LocalDate.now();
}
