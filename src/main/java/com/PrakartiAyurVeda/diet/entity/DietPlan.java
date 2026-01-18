package com.PrakartiAyurVeda.diet.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.PrakartiAyurVeda.common.enums.DoshaType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "dietPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<DailyDiet> dailyDiets = new ArrayList<>();

    @Column(columnDefinition = "LONGTEXT")
    private String avoidFoods;

    @Column(nullable = false)
    private LocalDate createdDate = LocalDate.now();

    public void setDailyDiets(List<DailyDiet> dailyDiets) {
        this.dailyDiets = dailyDiets;
        for (DailyDiet dailyDiet : dailyDiets) {
            dailyDiet.setDietPlan(this);
        }
    }
}
