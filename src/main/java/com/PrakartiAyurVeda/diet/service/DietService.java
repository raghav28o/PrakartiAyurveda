package com.PrakartiAyurVeda.diet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.assessment.repository.AssessmentRepository;
import com.PrakartiAyurVeda.common.enums.DoshaType;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.diet.repository.DietPlanRepository;

import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DietService {

    private final DietPlanRepository dietPlanRepository;
    private final AssessmentRepository assessmentRepository;

    @Transactional
    public DietPlan createDietPlan(Assessment assessment, String breakfast,
            String lunch, String dinner, String avoidFoods) {

        Long assessmentId = assessment.getId();

        // Prevent duplicate diet plan
        dietPlanRepository.findByAssessmentId(assessmentId)
                .ifPresent(dp -> {
                    throw new IllegalStateException(
                            "Diet plan already exists for assessment id: " + assessmentId
                    );
                });

        DietPlan dietPlan = new DietPlan();
        dietPlan.setAssessmentId(assessmentId);
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

    public DietPlan getById(Long id) {
        return dietPlanRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Diet plan not found with id: " + id
                        )
                );
    }

    public DietPlan getByAssessmentId(Long assessmentId) {
        return getDietByAssessmentId(assessmentId);
    }

    public DietPlan getByUserId(Long userId) {
        return dietPlanRepository.findLatestByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No diet plan found for user id: " + userId
                        )
                );
    }

    @Transactional
    public DietPlan updateDietPlan(Long assessmentId, String breakfast,
            String lunch, String dinner, String avoidFoods) {
//        System.out.println("Updating diet plan for assessment ID: " + assessmentId);

        System.out.println("New Breakfast: " + breakfast);
        System.out.println("New Lunch: " + lunch);
        System.out.println("New Dinner: " + dinner);
        System.out.println("New Avoid Foods: " + avoidFoods);

        // Use native SQL update query to avoid Hibernate entity management issues
//        int rowsUpdated = dietPlanRepository.updateDietPlanByAssessmentId(

//                assessmentId, breakfast, lunch, dinner, avoidFoods);

        DietPlan dietPlan = dietPlanRepository.findByAssessmentId(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("No diet plan found for assessment id: " + assessmentId));

        System.out.println("Found Diet Plan: " + dietPlan.getId());

        dietPlan.setBreakfast(breakfast);
        dietPlan.setLunch(lunch);
        dietPlan.setDinner(dinner);
        dietPlan.setAvoidFoods(avoidFoods);
//        dietPlanRepository.save(dietPlan);
        dietPlanRepository.save(dietPlan);


        // Fetch the updated diet plan to return
//        DietPlan updatedDietPlan = getDietByAssessmentId(assessmentId);
        System.out.println("Retrieved updated Diet Plan: " + dietPlan.getId());

        return dietPlan;
    }

    /**
     * Create a complete response Map with DietPlan and Assessment details
     */
    @Transactional
    public Map<String, Object> getDietPlanWithAssessment(Long assessmentId) {
        DietPlan dietPlan = getDietByAssessmentId(assessmentId);
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found for id: " + assessmentId));

        Map<String, Object> response = new HashMap<>();
        response.put("dietPlan", dietPlan);
        response.put("assessment", assessment);
        return response;
    }

    @Transactional
    public void deleteById(Long id) {
        if (!dietPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Diet plan not found with id: " + id);
        }
        dietPlanRepository.deleteById(id);
    }
}
