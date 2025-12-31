package com.PrakartiAyurVeda.diet.entity;

import java.time.LocalDate;

import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.common.enums.DoshaType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "diet_plans")
@Data
public class DietPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Assessment assessment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoshaType doshaType;

    @Column(nullable = false, length = 500)
    private String breakfast;

    @Column(nullable = false, length = 500)
    private String lunch;

    @Column(nullable = false, length = 500)
    private String dinner;

    @Column(length = 500)
    private String avoidFoods;

    @Column(nullable = false)
    private LocalDate createdDate = LocalDate.now();
}
