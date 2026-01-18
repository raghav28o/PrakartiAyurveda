package com.PrakartiAyurVeda.diet.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "daily_diets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyDiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String breakfast;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String lunch;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String dinner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_plan_id", nullable = false)
    @JsonBackReference
    private DietPlan dietPlan;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
