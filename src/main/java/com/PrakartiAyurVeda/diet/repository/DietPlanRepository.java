package com.PrakartiAyurVeda.diet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import org.springframework.stereotype.Repository;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {

    Optional<DietPlan> findByAssessment(Assessment assessment);

    Optional<DietPlan> findByAssessmentId(Long assessmentId);
}
