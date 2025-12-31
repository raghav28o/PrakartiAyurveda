package com.PrakartiAyurVeda.diet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.common.enums.DoshaType;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.diet.repository.DietPlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DietService {

    private final DietPlanRepository dietPlanRepository;

    @Transactional
    public DietPlan createDietPlan(Assessment assessment, String breakfast,
            String lunch, String dinner, String avoidFoods) {

        // Prevent duplicate diet plan
        dietPlanRepository.findByAssessment(assessment)
                .ifPresent(dp -> {
                    throw new IllegalStateException(
                            "Diet plan already exists for assessment id: " + assessment.getId()
                    );
                });

        DietPlan dietPlan = new DietPlan();
        dietPlan.setAssessment(assessment);
        dietPlan.setDoshaType(assessment.getDominantDosha());
        dietPlan.setBreakfast(breakfast);
        dietPlan.setLunch(lunch);
        dietPlan.setDinner(dinner);
        dietPlan.setAvoidFoods(avoidFoods);

        return dietPlanRepository.save(dietPlan);
    }

    public DietPlan getDietByAssessmentId(Long assessmentId) {
        return dietPlanRepository.findByAssessmentId(assessmentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No diet plan found for assessment id: " + assessmentId
                        )
                );
    }
}
