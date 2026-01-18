package com.PrakartiAyurVeda.diet.service;

import com.PrakartiAyurVeda.diet.dto.DailyDietDto;
import com.PrakartiAyurVeda.diet.dto.DietPlanDto;
import com.PrakartiAyurVeda.diet.entity.DailyDiet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.assessment.repository.AssessmentRepository;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.diet.repository.DietPlanRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Transactional
    public DietPlan createDietPlan(Assessment assessment, DietPlanDto dietPlanDto) {
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
        dietPlan.setBreakfast(dietPlanDto.getBreakfast());
        dietPlan.setLunch(dietPlanDto.getLunch());
        dietPlan.setDinner(dietPlanDto.getDinner());
        dietPlan.setAvoidFoods(dietPlanDto.getAvoidFoods());

        List<DailyDiet> dailyDiets = new ArrayList<>();
        if (dietPlanDto.getDailyDiets() != null) {
            for (DailyDietDto dailyDietDto : dietPlanDto.getDailyDiets()) {
                DailyDiet dailyDiet = DailyDiet.builder()
                        .dayOfWeek(dailyDietDto.getDayOfWeek())
                        .breakfast(dailyDietDto.getBreakfast())
                        .lunch(dailyDietDto.getLunch())
                        .dinner(dailyDietDto.getDinner())
                        .dietPlan(dietPlan)
                        .build();
                dailyDiets.add(dailyDiet);
            }
        }
        dietPlan.setDailyDiets(dailyDiets);

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

        DietPlan dietPlan = dietPlanRepository.findByAssessmentId(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("No diet plan found for assessment id: " + assessmentId));

        dietPlan.setBreakfast(breakfast);
        dietPlan.setLunch(lunch);
        dietPlan.setDinner(dinner);
        dietPlan.setAvoidFoods(avoidFoods);
        dietPlanRepository.save(dietPlan);

        return dietPlan;
    }

    @Transactional
    public DietPlan updateDietPlan(Long assessmentId, DietPlanDto dietPlanDto) {
        DietPlan dietPlan = dietPlanRepository.findByAssessmentId(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("No diet plan found for assessment id: " + assessmentId));

        dietPlan.setBreakfast(dietPlanDto.getBreakfast());
        dietPlan.setLunch(dietPlanDto.getLunch());
        dietPlan.setDinner(dietPlanDto.getDinner());
        dietPlan.setAvoidFoods(dietPlanDto.getAvoidFoods());

        // Don't clear existing daily diets, just add new ones
        List<DailyDiet> newDailyDiets = new ArrayList<>();
        if (dietPlanDto.getDailyDiets() != null) {
            for (DailyDietDto dailyDietDto : dietPlanDto.getDailyDiets()) {
                DailyDiet dailyDiet = DailyDiet.builder()
                        .dayOfWeek(dailyDietDto.getDayOfWeek())
                        .breakfast(dailyDietDto.getBreakfast())
                        .lunch(dailyDietDto.getLunch())
                        .dinner(dailyDietDto.getDinner())
                        .dietPlan(dietPlan)
                        .build();
                newDailyDiets.add(dailyDiet);
            }
            dietPlan.getDailyDiets().addAll(newDailyDiets);
        }

        DietPlan savedDietPlan = dietPlanRepository.save(dietPlan);

        // Return a diet plan with only the newly created daily diets
        DietPlan resultDietPlan = new DietPlan();
        resultDietPlan.setId(savedDietPlan.getId());
        resultDietPlan.setAssessmentId(savedDietPlan.getAssessmentId());
        resultDietPlan.setDoshaType(savedDietPlan.getDoshaType());
        resultDietPlan.setBreakfast(savedDietPlan.getBreakfast());
        resultDietPlan.setLunch(savedDietPlan.getLunch());
        resultDietPlan.setDinner(savedDietPlan.getDinner());
        resultDietPlan.setAvoidFoods(savedDietPlan.getAvoidFoods());
        resultDietPlan.setDailyDiets(newDailyDiets);

        return resultDietPlan;
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
