package com.PrakartiAyurVeda.diet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.PrakartiAyurVeda.diet.entity.DietPlan;
import org.springframework.stereotype.Repository;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {

    @Query("SELECT dp FROM DietPlan dp WHERE dp.assessmentId = :assessmentId")
    Optional<DietPlan> findByAssessmentId(@Param("assessmentId") Long assessmentId);

    @Query(value = "SELECT dp.* FROM diet_plans dp " +
            "INNER JOIN assessments a ON dp.assessment_id = a.id " +
            "WHERE a.user_id = :userId ORDER BY dp.created_date DESC LIMIT 1", nativeQuery = true)
    Optional<DietPlan> findLatestByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE diet_plans SET breakfast = :breakfast, lunch = :lunch, " +
            "dinner = :dinner, avoid_foods = :avoidFoods " +
            "WHERE assessment_id = :assessmentId", nativeQuery = true)
    int updateDietPlanByAssessmentId(
            @Param("assessmentId") Long assessmentId,
            @Param("breakfast") String breakfast,
            @Param("lunch") String lunch,
            @Param("dinner") String dinner,
            @Param("avoidFoods") String avoidFoods
    );

}
